package org.yi.happy.archive.tag;

/**
 * Mark lines inside the stream or regions.
 * 
 * Regions labeled line, with the newline characters between them. There is
 * always a line before any newline. After the final newline does not count as a
 * line unless there is something on it. Between pairs of newlines there are
 * empty lines marked.
 */
public class LineHandler extends AbstractStateMarkFilter {
    /**
     * The line label.
     */
    public static final String LINE = "line";

    /**
     * one of the end of line characters.
     */
    public static final byte CR = '\r';

    /**
     * one of the end of line characters.
     */
    public static final byte LF = '\n';

    /**
     * outside of a line and a newline sequence, right before the start of a
     * line.
     */
    private final State OUTSIDE = new State() {
        @Override
        public void startStream() {
            sendStartStream();
        }

        @Override
        public void startRegion(String name) {
            sendStartRegion(name);
        }

        @Override
        public void data(byte b) {
            if (b == CR) {
                sendStartRegion(LINE);
                sendEndRegion(LINE);
                setState(NL_CR);
                send();
                return;
            }

            if (b == LF) {
                sendStartRegion(LINE);
                sendEndRegion(LINE);
                setState(NL_LF);
                send();
                return;
            }

            sendStartRegion(LINE);
            setState(INSIDE);
            send();
            return;
        }

        @Override
        public void endRegion(String name) {
            sendEndRegion(name);
        }

        @Override
        public void endStream() {
            sendEndStream();
        }
    };

    /**
     * inside a line.
     */
    private final State INSIDE = new State() {
        @Override
        public void startRegion(String name) {
            sendEndRegion(LINE);
            sendStartRegion(name);
            setState(OUTSIDE);
        }

        @Override
        public void data(byte b) {
            if (b == CR) {
                sendEndRegion(LINE);
                send();
                setState(NL_CR);
                return;
            }

            if (b == LF) {
                sendEndRegion(LINE);
                send();
                setState(NL_LF);
                return;
            }

            send();
            return;
        }

        @Override
        public void endRegion(String name) {
            sendEndRegion(LINE);
            sendEndRegion(name);
            setState(OUTSIDE);
        }

        @Override
        public void endStream() {
            sendEndRegion(LINE);
            sendEndStream();
            setState(OUTSIDE);
        }
    };

    /**
     * after the first carrier-return of a newline sequence.
     */
    private final State NL_CR = new State() {
        @Override
        public void startRegion(String name) {
            sendStartRegion(name);
            setState(OUTSIDE);
        }

        @Override
        public void data(byte b) {
            if (b == CR) {
                sendStartRegion(LINE);
                sendEndRegion(LINE);
                send();
                setState(NL_CR);
                return;
            }

            if (b == LF) {
                send();
                setState(OUTSIDE);
                return;
            }

            sendStartRegion(LINE);
            send();
            setState(INSIDE);
            return;
        }

        @Override
        public void endRegion(String name) {
            sendEndRegion(name);
            setState(OUTSIDE);
        }

        @Override
        public void endStream() {
            sendEndStream();
            setState(OUTSIDE);
        }
    };

    /**
     * after the first line-feed of a newline sequence.
     */
    private final State NL_LF = new State() {
        @Override
        public void startRegion(String name) {
            sendStartRegion(name);
            setState(OUTSIDE);
        }

        @Override
        public void data(byte b) {
            if (b == CR) {
                send();
                setState(OUTSIDE);
                return;
            }

            if (b == LF) {
                sendStartRegion(LINE);
                sendEndRegion(LINE);
                send();
                setState(NL_LF);
                return;
            }

            sendStartRegion(LINE);
            send();
            setState(INSIDE);
            return;
        }

        @Override
        public void endRegion(String name) {
            sendEndRegion(name);
            setState(OUTSIDE);
        }

        @Override
        public void endStream() {
            sendEndStream();
            setState(OUTSIDE);
        }
    };

    /**
     * set up {@link LineHandler} with a handler to accept the events.
     * 
     * @param handler
     *            the handler that accepts the events.
     */
    public LineHandler(BinaryHandler handler) {
        super(handler);
        setState(OUTSIDE);
    }
}
