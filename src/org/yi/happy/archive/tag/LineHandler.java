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
    private int state;
    
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
        state = 0;

        handler.startStream();
    }

    @Override
    public void startRegion(String name) {
        if (state == 1) {
            handler.endRegion("line");
            state = 0;
        }

        else if (state == 2 || state == 3) {
            state = 0;
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
            if (state == 0) {
                /*
                 * before line
                 */
                if (buff[i] == '\r') {
                    s = flush(buff, s, i);
                    handler.startRegion("line");
                    handler.endRegion("line");
                    state = 2;
                }

                else if (buff[i] == '\n') {
                    s = flush(buff, s, i);
                    handler.startRegion("line");
                    handler.endRegion("line");
                    state = 3;
                }

                else {
                    s = flush(buff, s, i);
                    handler.startRegion("line");
                    state = 1;
                }
            }

            else if (state == 1) {
                /*
                 * in line
                 */
                if (buff[i] == '\r') {
                    s = flush(buff, s, i);
                    handler.endRegion("line");
                    state = 2;
                }

                else if (buff[i] == '\n') {
                    s = flush(buff, s, i);
                    handler.endRegion("line");
                    state = 3;
                }
            }

            else if (state == 2) {
                /*
                 * in newline \r
                 */
                if (buff[i] == '\r') {
                    s = flush(buff, s, i);
                    handler.startRegion("line");
                    handler.endRegion("line");
                    state = 2;
                }

                else if (buff[i] == '\n') {
                    s = flush(buff, s, i + 1);
                    state = 0;
                }

                else {
                    s = flush(buff, s, i);
                    handler.startRegion("line");
                    state = 1;
                }
            }

            else if (state == 3) {
                /*
                 * in newline \n
                 */
                if (buff[i] == '\r') {
                    s = flush(buff, s, i + 1);
                    state = 0;
                }

                else if (buff[i] == '\n') {
                    s = flush(buff, s, i);
                    handler.startRegion("line");
                    handler.endRegion("line");
                    state = 3;
                }

                else {
                    s = flush(buff, s, i);
                    handler.startRegion("line");
                    state = 1;
                }
            }
        }

        s = flush(buff, s, offset + length);
    }

    @Override
    public void endRegion(String name) {
        if (state == 1) {
            handler.endRegion("line");
            state = 0;
        }

        else if (state == 2 || state == 3) {
            state = 0;
        }

        handler.endRegion(name);
    }

    @Override
    public void endStream() {
        if (state == 1) {
            handler.endRegion("line");
            state = 0;
        }

        else if (state == 2 || state == 3) {
            state = 0;
        }

        handler.endStream();
    }

}
