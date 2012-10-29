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
     * ignore (newline .. /newline), pass on ignored bytes.
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
    private static final int BETWEEN_RECORD = 0;
    private static final int FIELD_KEY = 1;
    private static final int FIELD_VALUE = 2;
    private static final int BETWEEN_FIELD = 3;
    private int state = BETWEEN_RECORD;

    @Override
    public void startStream() {
        if (inLine == false && state == BETWEEN_RECORD) {
            handler.startStream();
            return;
        }

        throw new IllegalStateException();
    }

    @Override
    public void startRegion(String name) {
        if (inLine == false && name.equals("line")) {
            inLine = true;
            return;
        }

        throw new IllegalStateException();
    }

    @Override
    public void bytes(byte[] buff, int offset, int length) {
        if (inLine == false) {
            handler.bytes(buff, offset, length);
            return;
        }

        if (length > 0) {
            if (state == BETWEEN_RECORD) {
                handler.startRegion("tag");
                handler.startRegion("field");
                handler.startRegion("key");
                state = FIELD_KEY;
            }

            if (state == BETWEEN_FIELD) {
                handler.startRegion("field");
                handler.startRegion("key");
                state = FIELD_KEY;
            }
        }

        int s = offset;
        for (int i = offset; i < offset + length; i++) {
            if (state == FIELD_KEY && buff[i] == '=') {
                if (s != i) {
                    handler.bytes(buff, s, i - s);
                }
                handler.endRegion("key");
                handler.bytes(buff, i, 1);
                s = i + 1;
                handler.startRegion("value");
                state = FIELD_VALUE;
            }
        }

        if (s != offset + length) {
            handler.bytes(buff, s, offset + length - s);
        }
    }

    @Override
    public void endRegion(String name) {
        if (state == BETWEEN_RECORD && name.equals("line")) {
            inLine = false;

            state = BETWEEN_RECORD;
            return;
        }

        if (state == FIELD_KEY && name.equals("line")) {
            inLine = false;
            handler.endRegion("key");
            handler.startRegion("value");
            handler.endRegion("value");
            handler.endRegion("field");

            state = BETWEEN_FIELD;
            return;
        }

        if (state == FIELD_VALUE && name.equals("line")) {
            inLine = false;
            handler.endRegion("value");
            handler.endRegion("field");

            state = BETWEEN_FIELD;
            return;
        }

        if (state == BETWEEN_FIELD && name.equals("line")) {
            inLine = false;
            handler.endRegion("tag");

            state = BETWEEN_RECORD;
            return;
        }

        throw new IllegalStateException();
    }

    @Override
    public void endStream() {
        if (state == BETWEEN_FIELD) {
            handler.endRegion("tag");
            handler.endStream();

            state = BETWEEN_RECORD;
            return;
        }

        if (state == BETWEEN_RECORD) {
            handler.endStream();

            state = BETWEEN_RECORD;
            return;
        }

        throw new IllegalStateException();
    }
}
