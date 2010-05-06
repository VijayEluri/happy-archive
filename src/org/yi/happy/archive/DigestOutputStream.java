package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

import org.yi.happy.archive.key.HashValue;

/**
 * An output stream that calculates the digest of whatever is written to it.
 */
public class DigestOutputStream extends OutputStream {
    private MessageDigest md;
    private HashValue hash;
    private long size;

    /**
     * set up an output stream that calculates the digest of whatever is written
     * to it.
     * 
     * @param md
     *            the {@link MessageDigest} to use. The {@link MessageDigest} is
     *            assumed to be freshly created and not shared.
     */
    public DigestOutputStream(MessageDigest md) {
        this.md = md;
        this.hash = null;
        this.size = 0;
    }

    @Override
    public void write(int b) throws IOException {
        if (md == null) {
            throw new ClosedException();
        }

        md.update((byte) b);
        size += 1;
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (md == null) {
            throw new ClosedException();
        }

        md.update(b);
        size += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (md == null) {
            throw new ClosedException();
        }

        md.update(b, off, len);
        size += len;
    }

    @Override
    public void close() throws IOException {
        if (md == null) {
            return;
        }

        hash = new HashValue(md.digest());
        md = null;
    }

    /**
     * Get the final digest value.
     * 
     * @return the final digest value.
     * @throws IllegalStateException
     *             if {@link #close()} has not been called.
     */
    public HashValue getHash() throws IllegalStateException {
        if (hash == null) {
            throw new IllegalStateException();
        }
        return hash;
    }

    /**
     * @return the number of bytes written to this stream.
     */
    public long getSize() {
        return size;
    }
}
