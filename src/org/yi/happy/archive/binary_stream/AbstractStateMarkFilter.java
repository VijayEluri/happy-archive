package org.yi.happy.archive.binary_stream;

/**
 * A template for a region marking stream parsing filter. This makes it easy to
 * translate a state diagram for a parser that inserts region markings into a
 * stream of bytes without changing the stream of bytes. To use this class
 * implement some state instances to handle the transitions.
 */
public abstract class AbstractStateMarkFilter implements BinaryHandler {
    protected static class State {
        public void startStream() {
            throw new IllegalStateException();
        }

        public void startRegion(String name) {
            throw new IllegalStateException();
        }

        public void data(byte b) {
            throw new IllegalStateException();
        }

        public void endRegion(String name) {
            throw new IllegalStateException();
        }

        public void endStream() {
            throw new IllegalStateException();
        }
    }

    private final BinaryHandler handler;
    private State state = new State();

    protected final void setState(State state) {
        this.state = state;
    }

    protected final State getState() {
        return state;
    }

    protected AbstractStateMarkFilter(BinaryHandler handler) {
        this.handler = handler;
    }

    @Override
    public final void startStream() {
        state.startStream();
    }

    protected final void sendStartStream() {
        flush();
        handler.startStream();
    }

    @Override
    public final void startRegion(String name) {
        state.startRegion(name);
    }

    protected final void sendStartRegion(String name) {
        flush();
        handler.startRegion(name);
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

    protected final void send() {
        if (sendBuff == null) {
            throw new UnsupportedOperationException();
        }

        if (sendCurrent != sendEnd) {
            flush();
        }

        sendEnd++;
    }

    private int sendStart;
    private int sendEnd;
    private byte[] sendBuff;
    private int sendCurrent;

    @Override
    public final void bytes(byte[] buff, int offset, int length) {
        sendStart = offset;
        sendEnd = offset;
        sendBuff = buff;
        try {
            for (sendCurrent = offset; sendCurrent < offset + length; sendCurrent++) {
                state.data(buff[sendCurrent]);
            }

            flush();
        } finally {
            sendBuff = null;
        }
    }

    @Override
    public final void endRegion(String name) {
        state.endRegion(name);
    }

    protected final void sendEndRegion(String name) {
        flush();
        handler.endRegion(name);
    }

    @Override
    public final void endStream() {
        state.endStream();
    }

    protected final void sendEndStream() {
        flush();
        handler.endStream();
    }
}
