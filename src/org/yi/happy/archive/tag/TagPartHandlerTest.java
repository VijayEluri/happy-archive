package org.yi.happy.archive.tag;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.yi.happy.archive.ByteString;

/**
 * Tests for {@link TagPartHandler}.
 */
public class TagPartHandlerTest {
    /**
     * Parse a single field single tag.
     */
    @Test
    public void testParseOne() {
        LogHandler log = new LogHandler();

        LineHandler p = new LineHandler(new TagPartHandler(log));
        p.startStream();
        p.bytes(ByteString.toBytes("a=b\n\n\n"), 0, 6);
        p.endStream();

        assertEquals(Arrays.asList("start", "start tag", "start field",
                "start key", "bytes a", "end key", "start value", "bytes b",
                "end value", "end field", "end tag", "end"), log.fetchLog());
    }

    /**
     * Parse a two field single tag.
     */
    @Test
    public void testParseTwoFields() {
        LogHandler log = new LogHandler();

        LineHandler p = new LineHandler(new TagPartHandler(log));
        p.startStream();
        p.bytes(ByteString.toBytes("a=b\nb=c\n\n"), 0, 9);
        p.endStream();

        assertEquals(Arrays.asList("start", "start tag", "start field",
                "start key", "bytes a", "end key", "start value", "bytes b",
                "end value", "end field", "start field", "start key",
                "bytes b", "end key", "start value", "bytes c", "end value",
                "end field", "end tag", "end"), log.fetchLog());
    }

    /**
     * parse a tag missing a value on a field.
     */
    @Test
    public void testParseBlankValue() {
        LogHandler log = new LogHandler();

        LineHandler p = new LineHandler(new TagPartHandler(log));
        p.startStream();
        p.bytes(ByteString.toBytes("a\n\n\n"), 0, 4);
        p.endStream();

        assertEquals(Arrays.asList("start", "start tag", "start field",
                "start key", "bytes a", "end key", "start value", "end value",
                "end field", "end tag", "end"), log.fetchLog());
    }

    /**
     * parse a stream with no tags.
     */
    @Test
    public void testParseNoTags() {
        LogHandler log = new LogHandler();

        LineHandler p = new LineHandler(new TagPartHandler(log));
        p.startStream();
        p.bytes(ByteString.toBytes("\n\n\n"), 0, 3);
        p.endStream();

        assertEquals(Arrays.asList("start", "end"), log.fetchLog());
    }
}
