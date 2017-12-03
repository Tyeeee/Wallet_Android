package com.yjt.wallet.core.message;

import com.yjt.wallet.core.exception.ProtocolException;

import java.util.List;

/**
 * The "getheaders" command is structurally identical to "getblocks", but has different meaning. On receiving this
 * message a Bitcoin node returns matching blocks up to the limit, but without the bodies. It is useful as an
 * optimization: when your wallet does not contain any keys created before a particular time, you don't have to download
 * the bodies for those blocks because you know there are no relevant transactions.
 */
public class GetHeadersMessage extends GetBlocksMessage {
    public GetHeadersMessage(List<byte[]> locator, byte[] stopHash) {
        super(locator, stopHash);
    }

    public GetHeadersMessage(byte[] msg) throws ProtocolException {
        super(msg);
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("getheaders: ");
        for (byte[] hash : locator) {
            b.append(hash.toString());
            b.append(" ");
        }
        return b.toString();
    }

    /**
     * Compares two getheaders messages. Note that even though they are structurally identical a GetHeadersMessage
     * will not compare equal to a GetBlocksMessage containing the same data.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        GetHeadersMessage other = (GetHeadersMessage) o;
        return (other.version == version &&
                locator.size() == other.locator.size() && locator.containsAll(other.locator) &&
                stopHash.equals(other.stopHash));
    }

    @Override
    public int hashCode() {
        int hashCode = (int) version ^ "getheaders".hashCode();
        for (byte[] aLocator : locator) hashCode ^= aLocator.hashCode();
        hashCode ^= stopHash.hashCode();
        return hashCode;
    }
}
