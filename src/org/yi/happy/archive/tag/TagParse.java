package org.yi.happy.archive.tag;

import java.util.ArrayList;
import java.util.List;

/**
 * A parser for archive tags, the data format that describes directories.
 */
public class TagParse {
    /*
     * The legacy tag parser used 8-bit characters and '%xx' escaped byte values
     * (0x00..0x19, 0x7f..0xff, '%', '='), this one does not.
     * 
     * This parser uses UTF-8 character encoding and no escaping at this time,
     * though escaping may be introduced in the future.
     */

    /**
     * Parse all of the tags from a block of bytes.
     * 
     * @param d
     *            the bytes to parse.
     * @return the list of tags.
     */
    public static List<Tag> parse(byte[] d) {
        TagParser p = new TagParser();
        p.bytes(d, 0, d.length);
        p.finish();
        ArrayList<Tag> out = new ArrayList<Tag>();
        while (p.isReady()) {
            out.add(p.get());
        }
        return out;
    }
}
