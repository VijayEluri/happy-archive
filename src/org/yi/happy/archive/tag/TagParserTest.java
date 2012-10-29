package org.yi.happy.archive.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.yi.happy.archive.ByteString;

/**
 * Tests for {@link TagParser}.
 */
public class TagParserTest {
    /**
     * parse a complete tag as it arrives.
     */
    @Test
    public void testParseOne() {
        TagParser p = new TagParser();

        assertFalse(p.isReady());

        byte[] data = ByteString.toBytes("a=b\n" + "b=c\n" + "\n");
        p.bytes(data, 0, data.length);

        assertTrue(p.isReady());

        Tag have = p.get();
        Tag want = new TagBuilder().add("a", "b").add("b", "c").create();

        assertEquals(want, have);

        assertFalse(p.isReady());
    }

    /**
     * parse one and a half tags, also and finish the stream to get the final
     * one.
     */
    @Test
    public void testParseOneAndAHalf() {
        TagParser p = new TagParser();

        assertFalse(p.isReady());

        byte[] data = ByteString.toBytes("a=b\n" + "b=c\n" + "\n" + "a=c\n");
        p.bytes(data, 0, data.length);

        assertTrue(p.isReady());

        Tag have = p.get();
        Tag want = new TagBuilder().add("a", "b").add("b", "c").create();

        assertEquals(want, have);

        assertFalse(p.isReady());

        p.finish();

        assertTrue(p.isReady());

        have = p.get();
        want = new TagBuilder().add("a", "c").create();

        assertEquals(want, have);
    }
}
