package org.yi.happy.archive.tag;

/**
 * Take a binary stream with line markings and add the regions for the parts of
 * a tag. Emits events for tag(field(key(#text), value(#text))).
 */
public class TagPartHandler implements BinaryHandler {
    /*
     * The syntax of a tag is simple, key=value pairs divided by newlines, and
     * groups divided by blank lines.
     */

    /*
     * ignore (newline .. /newline), ignore (line)
     * 
     * states
     * 
     * 0 - between records (data -> 1) (/line -> 0) (end -> 0)
     * 
     * 1 - key part ('=' -> 2) (/line -> 3)
     * 
     * 2 - value part (/line -> 3)
     * 
     * 3 - between fields (data -> 1) (/line -> 0) (end -> 0)
     */

    private final BinaryHandler handler;

    /**
     * connect this event filter to a handler.
     * 
     * @param handler
     *            the handler to receive the tag parsing events.
     */
    public TagPartHandler(BinaryHandler handler) {
        this.handler = handler;
    }

    int inNewline = 0;
    int state = 0;

    @Override
    public void startStream() {
        handler.startStream();
    }

    @Override
    public void startRegion(String name) {
        if (name.equals("newline")) {
            inNewline++;
            return;
        }
        if (inNewline > 0) {
            return;
        }

        if (name.equals("line")) {
            return;
        }

        throw new IllegalStateException();
    }

    @Override
    public void bytes(byte[] buff, int offset, int length) {
        if (inNewline > 0) {
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
                    handler.startRegion("value");
                    s = i + 1;
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
                    handler.startRegion("value");
                    s = i + 1;
                    state = 2;
                }
            }

            else if (state == 3) {
                if (buff[i] == '=') {
                    handler.startRegion("field");
                    handler.startRegion("key");
                    handler.endRegion("key");
                    handler.startRegion("value");
                    s = i + 1;
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
        if (name.equals("newline")) {
            inNewline--;
            return;
        }
        if (inNewline > 0) {
            return;
        }

        if (state == 0 && name.equals("line")) {
            return;
        }

        if (state == 1 && name.equals("line")) {
            handler.endRegion("key");
            handler.startRegion("value");
            handler.endRegion("value");
            handler.endRegion("field");

            state = 3;
            return;
        }

        if (state == 2 && name.equals("line")) {
            handler.endRegion("value");
            handler.endRegion("field");

            state = 3;
            return;
        }

        if (state == 3 && name.equals("line")) {
            handler.endRegion("tag");

            state = 0;
            return;
        }

        throw new IllegalStateException();
    }

    @Override
    public void endStream() {
        if (state == 1) {
            handler.endRegion("key");
            handler.startRegion("value");
            handler.endRegion("value");
            handler.endRegion("field");
            handler.endRegion("tag");

            state = 0;
        }

        else if (state == 2) {
            handler.endRegion("value");
            handler.endRegion("field");
            handler.endRegion("tag");

            state = 0;
        }

        else if (state == 3) {
            handler.endRegion("tag");

            state = 0;
        }

        handler.endStream();
    }
}
