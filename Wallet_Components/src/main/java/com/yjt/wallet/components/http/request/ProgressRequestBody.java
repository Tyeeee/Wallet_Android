package com.yjt.wallet.components.http.request;

import com.yjt.wallet.components.http.listener.OnUpdateProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class ProgressRequestBody extends RequestBody {

    private long time;
    private RequestBody requestBody;
    private OnUpdateProgressListener onUpdateProgressListener;

    public ProgressRequestBody(RequestBody body, OnUpdateProgressListener listener) {
        this.requestBody = body;
        this.onUpdateProgressListener = listener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return requestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        time = System.currentTimeMillis();
        BufferedSink newSink = Okio.buffer(new CountingSink(sink));
        requestBody.writeTo(newSink);
        newSink.flush();
    }

    private final class CountingSink extends ForwardingSink {

        private long writtenBytes;
        private long dataLength;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            if (dataLength == 0) {
                dataLength = contentLength();
            }
            writtenBytes += byteCount;
            if (onUpdateProgressListener != null) {
                long totalTime = (System.currentTimeMillis() - time) / 1000;
                if (totalTime == 0) {
                    totalTime += 1;
                }
                long networkSpeed = writtenBytes / totalTime;
                int progress = (int) (writtenBytes * 100 / dataLength);
                boolean done = writtenBytes == dataLength;
                onUpdateProgressListener.updateProgress(progress, networkSpeed, done);
            }
        }
    }
}
