package org.yi.happy.archive.tag;

/**
 * Mark lines inside the stream or regions.
 * 
 * Regions labeled line, with the newline characters between them. There is
 * always a line before any newline. After the final newline does not count as a
 * line unless there is something on it. Between pairs of newlines there are
 * empty lines marked.
 */
public class LineHandler implements BinaryHandler {
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

    private static final int OUTSIDE = 0;
    private static final int INSIDE = 1;
    private static final int NL_CR = 2;
    private static final int NL_LF = 3;
    private int state = OUTSIDE;

    private final BinaryHandler handler;

    /**
     * set up {@link LineHandler} with a handler to accept the events.
     * 
     * @param handler
     *            the handler that accepts the events.
     */
    public LineHandler(BinaryHandler handler) {
        this.handler = handler;
    }

    @Override
    public void startStream() {
        state = OUTSIDE;

        handler.startStream();
    }

    @Override
    public void startRegion(String name) {
        if (state == INSIDE) {
            handler.endRegion(LINE);
            state = OUTSIDE;
        }

        else if (state == NL_CR || state == NL_LF) {
            state = OUTSIDE;
        }

        handler.startRegion(name);
    }

    private int flush(byte[] buff, int start, int end) {
        if (start == end) {
            return end;
        }

        handler.bytes(buff, start, end - start);
        return end;
    }

    @Override
    public void bytes(byte[] buff, int offset, int length) {
        int s = offset;

        for (int i = offset; i < offset + length; i++) {
            if (state == OUTSIDE) {
                if (buff[i] == CR) {
                    s = flush(buff, s, i);
                    handler.startRegion(LINE);
                    handler.endRegion(LINE);
                    state = NL_CR;
                }

                else if (buff[i] == LF) {
                    s = flush(buff, s, i);
                    handler.startRegion(LINE);
                    handler.endRegion(LINE);
                    state = NL_LF;
                }

                else {
                    s = flush(buff, s, i);
                    handler.startRegion(LINE);
                    state = INSIDE;
                }
            }

            else if (state == INSIDE) {
                if (buff[i] == CR) {
                    s = flush(buff, s, i);
                    handler.endRegion(LINE);
                    state = NL_CR;
                }

                else if (buff[i] == LF) {
                    s = flush(buff, s, i);
                    handler.endRegion(LINE);
                    state = NL_LF;
                }
            }

            else if (state == NL_CR) {
                if (buff[i] == CR) {
                    s = flush(buff, s, i);
                    handler.startRegion(LINE);
                    handler.endRegion(LINE);
                    state = NL_CR;
                }

                else if (buff[i] == LF) {
                    s = flush(buff, s, i + 1);
                    state = OUTSIDE;
                }

                else {
                    s = flush(buff, s, i);
                    handler.startRegion(LINE);
                    state = INSIDE;
                }
            }

            else if (state == NL_LF) {
                if (buff[i] == CR) {
                    s = flush(buff, s, i + 1);
                    state = OUTSIDE;
                }

                else if (buff[i] == LF) {
                    s = flush(buff, s, i);
                    handler.startRegion(LINE);
                    handler.endRegion(LINE);
                    state = NL_LF;
                }

                else {
                    s = flush(buff, s, i);
                    handler.startRegion(LINE);
                    state = INSIDE;
                }
            }
        }

        s = flush(buff, s, offset + length);
    }

    @Override
    public void endRegion(String name) {
        if (state == INSIDE) {
            handler.endRegion(LINE);
            state = OUTSIDE;
        }

        else if (state == NL_CR || state == NL_LF) {
            state = OUTSIDE;
        }

        handler.endRegion(name);
    }

    @Override
    public void endStream() {
        if (state == INSIDE) {
            handler.endRegion(LINE);
            state = OUTSIDE;
        }

        else if (state == NL_CR || state == NL_LF) {
            state = OUTSIDE;
        }

        handler.endStream();
    }

}
