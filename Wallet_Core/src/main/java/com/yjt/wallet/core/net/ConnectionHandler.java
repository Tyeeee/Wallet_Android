/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yjt.wallet.core.net;

import com.google.common.base.Throwables;

import net.bither.bitherj.message.Message;
import net.bither.bitherj.utils.Threading;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * A simple NIO MessageWriteTarget which handles all the business logic of a connection
 * (reading+writing bytes).
 * Used only by the NioClient and NioServer classes
 */
class ConnectionHandler implements MessageWriteTarget {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ConnectionHandler.class);

    private static final int BUFFER_SIZE_LOWER_BOUND = 4096;
    private static final int BUFFER_SIZE_UPPER_BOUND = 65536;

    private static final int OUTBOUND_BUFFER_BYTE_COUNT = Message.MAX_SIZE + 24; // 24 byte
    // message header

    // We lock when touching local flags and when writing data, but NEVER when calling any
    // methods which leave this
    // class into non-Java classes.
    private final ReentrantLock lock = Threading.lock("nioConnectionHandler");
    @GuardedBy("lock")
    private final ByteBuffer readBuff;
    @GuardedBy("lock")
    private final SocketChannel channel;
    @GuardedBy("lock")
    private final SelectionKey key;
    @GuardedBy("lock")
    StreamParser parser;
    @GuardedBy("lock")
    private boolean closeCalled = false;

    @GuardedBy("lock")
    private long bytesToWriteRemaining = 0;
    @GuardedBy("lock")
    private final LinkedList<ByteBuffer> bytesToWrite = new LinkedList<ByteBuffer>();

    private Set<ConnectionHandler> connectedHandlers;

    private ConnectionHandler(@Nullable StreamParser parser, SelectionKey key) {
        this.key = key;
        this.channel = checkNotNull(((SocketChannel) key.channel()));
        if (parser == null) {
            readBuff = null;
            closeConnection();
            return;
        }
        this.parser = parser;
        readBuff = ByteBuffer.allocateDirect(Math.min(Math.max(parser.getMaxMessageSize(),
                BUFFER_SIZE_LOWER_BOUND), BUFFER_SIZE_UPPER_BOUND));
        parser.setWriteTarget(this); // May callback into us (eg closeConnection() now)
        connectedHandlers = null;
    }

    public ConnectionHandler(StreamParser parser, SelectionKey key,
                             Set<ConnectionHandler> connectedHandlers) {
        this(checkNotNull(parser), key);

        // closeConnection() may have already happened, in which case we shouldn't add ourselves
        // to the connectedHandlers set
        lock.lock();
        boolean alreadyClosed = false;
        try {
            alreadyClosed = closeCalled;
            this.connectedHandlers = connectedHandlers;
        } finally {
            lock.unlock();
        }
        if (!alreadyClosed) {
            checkState(connectedHandlers.add(this));
        }
    }

    @GuardedBy("lock")
    private void setWriteOps() {
        // Make sure we are registered to get updated when writing is available again
        key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
        // Refresh the selector to make sure it gets the new interestOps
        key.selector().wakeup();
    }

    // Tries to write any outstanding write bytes, runs in any thread (possibly unlocked)
    private void tryWriteBytes() throws IOException {
        lock.lock();
        try {
            // Iterate through the outbound ByteBuff queue, pushing as much as possible into the
            // OS' network buffer.
            Iterator<ByteBuffer> bytesIterator = bytesToWrite.iterator();
            while (bytesIterator.hasNext()) {
                ByteBuffer buff = bytesIterator.next();
                bytesToWriteRemaining -= channel.write(buff);
                if (!buff.hasRemaining()) {
                    bytesIterator.remove();
                } else {
                    setWriteOps();
                    break;
                }
            }
            // If we are done writing, clear the OP_WRITE interestOps
            if (bytesToWrite.isEmpty()) {
                key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
            }
            // Don't bother waking up the selector here, since we're just removing an op, not adding
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void writeBytes(byte[] message) throws IOException {
        lock.lock();
        try {
            // Network buffers are not unlimited (and are often smaller than some messages we may
            // wish to send), and
            // thus we have to buffer outbound messages sometimes. To do this,
            // we use a queue of ByteBuffers and just
            // append to it when we want to send a message. We then let tryWriteBytes() either
            // send the message or
            // register our SelectionKey to wakeup when we have free outbound buffer space
            // available.

            if (bytesToWriteRemaining + message.length > OUTBOUND_BUFFER_BYTE_COUNT) {
                throw new IOException("Outbound buffer overflowed");
            }
            // Just dump the message onto the write buffer and call tryWriteBytes
            // TODO: Kill the needless message duplication when the write completes right away
            bytesToWrite.offer(ByteBuffer.wrap(Arrays.copyOf(message, message.length)));
            bytesToWriteRemaining += message.length;
            setWriteOps();
        } catch (IOException e) {
            lock.unlock();
            log.error("Error writing message to connection, closing connection. IOException");
            closeConnection();
            throw e;
        } catch (CancelledKeyException e) {
            lock.unlock();
            log.error("Error writing message to connection, closing connection. CancelledKeyException");
            closeConnection();
            throw new IOException(e);
        }
        lock.unlock();
    }

    @Override
    // May NOT be called with lock held
    public void closeConnection() {
        try {
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connectionClosed();
    }

    private void connectionClosed() {
        boolean callClosed = false;
        lock.lock();
        try {
            callClosed = !closeCalled;
            closeCalled = true;
        } finally {
            lock.unlock();
        }
        if (callClosed) {
            checkState(connectedHandlers == null || connectedHandlers.remove(this));
            parser.connectionClosed();
        }
    }

    // Handle a SelectionKey which was selected
    // Runs unlocked as the caller is single-threaded (or if not, should enforce that handleKey
    // is only called
    // atomically for a given ConnectionHandler)
    public static void handleKey(SelectionKey key) {
        ConnectionHandler handler = ((ConnectionHandler) key.attachment());
        try {
            if (handler == null) {
                return;
            }
            if (!key.isValid()) {
                handler.closeConnection(); // Key has been cancelled,
                // make sure the socket gets closed
                return;
            }
            if (key.isReadable()) {
                // Do a socket read and invoke the parser's receiveBytes message
                int read = handler.channel.read(handler.readBuff);
                if (read == 0) {
                    return; // Was probably waiting on a write
                } else if (read == -1) { // Socket was closed
                    key.cancel();
                    handler.closeConnection();
                    return;
                }
                // "flip" the buffer - setting the limit to the current position and setting
                // position to 0
                handler.readBuff.flip();
                // Use parser.receiveBytes's return value as a check that it stopped reading at
                // the right location
                int bytesConsumed = checkNotNull(handler.parser).receiveBytes(handler.readBuff);
                checkState(handler.readBuff.position() == bytesConsumed);
                // Now drop the bytes which were read by compacting readBuff (resetting limit and
                // keeping relative
                // position)
                handler.readBuff.compact();
            }
            if (key.isWritable()) {
                handler.tryWriteBytes();
            }
        } catch (Exception e) {
            // This can happen eg if the channel closes while the thread is about to get killed
            // (ClosedByInterruptException), or if handler.parser.receiveBytes throws something
            if (!(e instanceof CancelledKeyException)) {
                log.error("Error handling SelectionKey: {}", Throwables.getRootCause(e).getMessage());
            }
            handler.closeConnection();
        }
    }
}
