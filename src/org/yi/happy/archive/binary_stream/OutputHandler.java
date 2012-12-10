package org.yi.happy.archive.binary_stream;

import org.yi.happy.archive.tag.BinaryHandler;

/**
 * The call back implementation used by {@link StateByteFilter} to get events
 * back from {@link State}.
 */
public class OutputHandler implements ActionCallback {
    private final BinaryHandler handler;

    /**
     * Create the call back.
     * 
     * @param handler
     *            where to pass the events on to.
     */
    public OutputHandler(BinaryHandler handler) {
        this.handler = handler;
    }

    @Override
    public void startStream() {
        flush();
        handler.startStream();
    }

    @Override
    public void endStream() {
        flush();
        handler.endStream();
    }

    @Override
    public void startRegion(String name) {
        flush();
        handler.startRegion(name);
    }

    @Override
    public void endRegion(String name) {
        flush();
        handler.endRegion(name);
    }

    /*
     * support for processing the bytes individually but sending them in
     * blocks when possible.
     * 
     * Interacts tightly with InterpretFilter.bytes(byte[], int, int).
     */

    private byte[] sendBuff;
    private int sendStart;
    private int sendCurrent;
    private int sendEnd;
    private int sendLimit;

    /**
     * flush the outstanding bytes if any. There will only be bytes if a block
     * of bytes is currently being processed.
     */
    public void flush() {
        if (sendBuff == null) {
            return;
        }

        if (sendStart != sendEnd) {
            handler.bytes(sendBuff, sendStart, sendEnd - sendStart);
        }

        sendStart = sendCurrent;
        sendEnd = sendCurrent;
    }

    @Override
    public void consumeAndSend() {
        if (sendBuff == null) {
            throw new UnsupportedOperationException();
        }

        if (sendCurrent != sendEnd) {
            flush();
        }

        if (sendCurrent >= sendLimit) {
            throw new IndexOutOfBoundsException();
        }

        sendEnd++;
        sendCurrent++;
    }

    /**
     * Start processing a block of bytes. The processing logic is in the caller
     * ({@link StateByteFilter}).
     * 
     * @param buff
     *            the buffer the block of bytes is in.
     * @param offset
     *            where the block of bytes begins in the buffer.
     * @param length
     *            how long the block of bytes is.
     */
    public void startBytes(byte[] buff, int offset, int length) {
        sendStart = offset;
        sendCurrent = offset;
        sendEnd = offset;
        sendLimit = offset + length;
        sendBuff = buff;
    }

    /**
     * 
     * @return true if there is a byte to process.
     */
    public boolean hasCurrentByte() {
        return sendCurrent < sendLimit;
    }

    /**
     * 
     * @return the current byte to process.
     */
    public byte getCurrentByte() {
        return sendBuff[sendCurrent];
    }

    /**
     * Finish processing the block of bytes. The processing logic is in the
     * caller ({@link StateByteFilter}).
     */
    public void endBytes() {
        try {
            flush();
        } finally {
            sendBuff = null;
        }
    }
}