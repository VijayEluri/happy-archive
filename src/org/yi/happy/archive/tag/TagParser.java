package org.yi.happy.archive.tag;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A push-pull parser for tags from a stream of bytes.
 */
public class TagParser {
    /**
     * The waiting parsed tags.
     */
    private Queue<Tag> out = new ArrayDeque<Tag>();

    /**
     * Captures the parsed tags from the end of the parsing pipeline.
     */
    private TagStreamVisitor capture = new TagStreamVisitor() {
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
}
