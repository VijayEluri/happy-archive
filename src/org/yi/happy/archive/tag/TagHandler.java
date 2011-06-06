package org.yi.happy.archive.tag;

/**
 * Re-mark a binary stream from lines to tags. Expects ( line, newline ). Emits
 * ( tag ( field ( key, value ) ) ).
 * 
 * The syntax of a tag is simple, key=value pairs divided by newlines, and
 * groups divided by blank lines.
 */
public class TagHandler implements BinaryHandler {
    /*
     * ignore (newline .. /newline, line), pass on ignored bytes.
     * 
     * states:
     * 
     * 0 - between records ('=' -> 2) (. -> 1) (/line -> 0) (end -> 0)
     * 
     * 1 - key part ('=' -> 2) (/line -> 3)
     * 
     * 2 - value part (/line -> 3)
     * 
     * 3 - between fields ('=' -> 2) (. -> 1) (/line -> 0) (end -> 0)
     */

    private final BinaryHandler handler;

    /**
     * connect this event filter to a handler.
     * 
     * @param handler
     *            the handler to receive the tag parsing events.
     */
    public TagHandler(BinaryHandler handler) {
        this.handler = handler;
    }

    private boolean inLine = false;
    private int state = 0;

    @Override
    public void startStream() {
        if (state == 0) {
            handler.startStream();
            return;
        }

        throw new IllegalStateException();
    }

    @Override
    public void startRegion(String name) {
        if (!inLine && name.equals("line")) {
            inLine = true;
            return;
        }

        throw new IllegalStateException();
    }

    @Override
    public void bytes(byte[] buff, int offset, int length) {
        if (!inLine) {
            handler.bytes(buff, offset, length);
            return;
        }

        int s = offset;
        for (int i = offset; i < offset + length; i++) {
            if (state == 0) {
                if (buff[i] == '=') {
                    handler.startRegion("tag");
                    handler.startRegion("field");
                    handler.startRegion("key");
                    handler.endRegion("key");
                    handler.bytes(buff, i, 1);
                    s = i + 1;
                    handler.startRegion("value");
                    state = 2;
                }

                else {
                    handler.startRegion("tag");
                    handler.startRegion("field");
                    handler.startRegion("key");
                    state = 1;
                }
            }

            else if (state == 1) {
                if (buff[i] == '=') {
                    if (s != i) {
                        handler.bytes(buff, s, i - s);
                    }
                    handler.endRegion("key");
                    handler.bytes(buff, i, 1);
                    s = i + 1;
                    handler.startRegion("value");
                    state = 2;
                }
            }

            else if (state == 3) {
                if (buff[i] == '=') {
                    handler.startRegion("field");
                    handler.startRegion("key");
                    handler.endRegion("key");
                    handler.bytes(buff, i, 1);
                    s = i + 1;
                    handler.startRegion("value");
                    state = 2;
                }

                else {
                    handler.startRegion("field");
                    handler.startRegion("key");
                    state = 1;
                }
            }
        }

        if (s != offset + length) {
            handler.bytes(buff, s, offset + length - s);
        }
    }

    @Override
    public void endRegion(String name) {
        if (state == 0 && name.equals("line")) {
            inLine = false;
            return;
        }

        if (state == 1 && name.equals("line")) {
            inLine = false;
            handler.endRegion("key");
            handler.startRegion("value");
            handler.endRegion("value");
            handler.endRegion("field");

            state = 3;
            return;
        }

        if (state == 2 && name.equals("line")) {
            inLine = false;
            handler.endRegion("value");
            handler.endRegion("field");

            state = 3;
            return;
        }

        if (state == 3 && name.equals("line")) {
            inLine = false;
            handler.endRegion("tag");

            state = 0;
            return;
        }

        throw new IllegalStateException();
    }

    @Override
    public void endStream() {
        if (state == 0) {
            handler.endStream();
            return;
        }

        if (state == 3) {
            handler.endRegion("tag");
            handler.endStream();
            state = 0;
            return;
        }

        throw new IllegalStateException();
    }
}
