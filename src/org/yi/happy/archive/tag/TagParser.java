package org.yi.happy.archive.tag;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * A push-pull parser for tags from a stream of bytes.
 */
public class TagParser {
    /*
     * The legacy tag parser used 8-bit characters and '%xx' escaped byte values
     * (0x00..0x19, 0x7f..0xff, '%', '='), this one does not.
     * 
     * This parser uses UTF-8 character encoding and no escaping at this time,
     * though escaping may be introduced in the future.
     */

    /**
     * The waiting parsed tags.
     */
    private Queue<Tag> out = new ArrayDeque<Tag>();

    /**
     * Captures the parsed tags from the end of the parsing pipeline.
     */
    private TagVisitor capture = new TagVisitor() {
        @Override
        public void accept(Tag tag) {
            out.add(tag);
        }
    };
    
    /**
     * The parsing pipeline.
     */
    private BinaryHandler parse = new LineHandler(new TagHandler(
            new TagCapture(capture)));

    /**
     * Create and start the pipeline.
     */
    public TagParser() {
        parse.startStream();
    }

    /**
     * parse some data.
     * 
     * @param buff
     *            the data buffer.
     * @param offset
     *            the offset in the buffer where the data is.
     * @param length
     *            the length of the data in the buffer.
     */
    public void bytes(byte[] buff, int offset, int length) {
        parse.bytes(buff, offset, length);
    }

    /**
     * Finish the pipeline, signal that there is no more data.
     */
    public void finish() {
        parse.endStream();
    }

    /**
     * find if there is a tag completely parsed.
     * 
     * @return true if there is a completely parsed tag ready.
     */
    public boolean isReady() {
        return out.isEmpty() == false;
    }

    /**
     * get the waiting parsed tag.
     * 
     * @return the parsed tag.
     */
    public Tag get() {
        return out.remove();
    }

    /**
     * Convenience method to parse only a single block of bytes into a list.
     * 
     * @param buff
     *            the single block of bytes.
     * @return the parsed tags.
     */
    public List<Tag> parse(byte[] buff) {
        bytes(buff, 0, buff.length);
        finish();

        List<Tag> out = new ArrayList<Tag>();
        while (isReady()) {
            out.add(get());
        }
        return out;
    }
}
