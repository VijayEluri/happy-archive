package org.yi.happy.archive.sandbox.interpret;

import org.yi.happy.archive.tag.BinaryHandler;

/**
 * A rule based interpreter for doing simple transformations to a labeled binary
 * stream.
 */
public class InterpretFilter implements BinaryHandler {

    private class OutputHandler implements ActionCallback {
        private final BinaryHandler handler;

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

        public void startBytes(byte[] buff, int offset, int length) {
            sendStart = offset;
            sendCurrent = offset;
            sendEnd = offset;
            sendLimit = offset + length;
            sendBuff = buff;
        }

        public boolean hasCurrentByte() {
            return sendCurrent < sendLimit;
        }

        public byte getCurrentByte() {
            return sendBuff[sendCurrent];
        }

        public void endBytes() {
            try {
                flush();
            } finally {
                sendBuff = null;
            }
        }
    }

    private OutputHandler callback;

    private State state;

    /**
     * set up a rule based interpreter for doing simple transformations to a
     * labeled binary stream using a finite state machine.
     * 
     * @param state
     *            the starting state.
     * @param handler
     *            the output handler.
     */
    public InterpretFilter(State state, BinaryHandler handler) {
        this.callback = new OutputHandler(handler);
        this.state = state;
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
        callback.startBytes(buff, offset, length);
        try {
            while (callback.hasCurrentByte()) {
                state = state.data(callback.getCurrentByte(), callback);
            }
        } finally {
            callback.endBytes();
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
