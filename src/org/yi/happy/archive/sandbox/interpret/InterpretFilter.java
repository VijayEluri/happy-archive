package org.yi.happy.archive.sandbox.interpret;

import org.yi.happy.archive.tag.BinaryHandler;

/**
 * A rule based interpreter for doing simple transformations to a labeled binary
 * stream.
 */
public class InterpretFilter implements BinaryHandler {

    private final BinaryHandler handler;
    private final ActionCallback callback = new ActionCallback() {
        @Override
        public void startStream() {
            sendStartStream();
        }

        @Override
        public void endStream() {
            sendEndStream();
        }

        @Override
        public void consumeAndSend() {
            sendData();
        }

        @Override
        public void startRegion(String name) {
            sendStartRegion(name);
        }

        @Override
        public void endRegion(String name) {
            sendEndRegion(name);
        }
    };

    private RuleState state;

    /**
     * set up a rule based interpreter for doing simple transformations to a
     * labeled binary stream using a finite state machine.
     * 
     * @param state
     *            the starting state.
     * @param handler
     *            the output handler.
     */
    public InterpretFilter(RuleState state, BinaryHandler handler) {
        this.handler = handler;
        this.state = state;
    }

    protected void sendEndRegion(String name) {
        flush();
        handler.endRegion(name);
    }

    protected void sendStartRegion(String name) {
        flush();
        handler.startRegion(name);
    }

    private byte[] sendBuff;
    private int sendStart;
    private int sendCurrent;
    private int sendEnd;

    protected void sendData() {
        if (sendBuff == null) {
            throw new UnsupportedOperationException();
        }

        if (sendCurrent != sendEnd) {
            flush();
        }

        sendEnd++;
        sendCurrent++;
    }

    private void flush() {
        if (sendBuff == null) {
            return;
        }

        if (sendStart != sendEnd) {
            handler.bytes(sendBuff, sendStart, sendEnd - sendStart);
        }
        sendStart = sendCurrent;
        sendEnd = sendCurrent;
    }

    protected void sendEndStream() {
        flush();
        handler.endStream();
    }

    protected void sendStartStream() {
        flush();
        handler.startStream();
    }

    @Override
    public void startStream() {
        state = state.startStream(callback);
    }

    @Override
    public void startRegion(String name) {
        state = state.startRegion(name, callback);
    }

    @Override
    public void bytes(byte[] buff, int offset, int length) {
        int end = offset + length;
        sendStart = offset;
        sendCurrent = offset;
        sendEnd = offset;
        sendBuff = buff;
        try {
            while (sendCurrent < end) {
                state = state.data(buff[sendCurrent], callback);
            }
            flush();
        } finally {
            sendBuff = null;
        }
    }

    @Override
    public void endRegion(String name) {
        state = state.endRegion(name, callback);
    }

    @Override
    public void endStream() {
        state = state.endStream(callback);
    }

}
