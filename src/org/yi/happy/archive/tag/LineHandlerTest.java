package org.yi.happy.archive.tag;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.binary_stream.LogHandler;

/**
 * Tests for {@link LineHandler}.
 */
public class LineHandlerTest {

    /**
     * A line, a blank line, a two character newline, and a line
     */
    @Test
    public void testBasicRun() {
        LogHandler log = new LogHandler();
        LineHandler h = new LineHandler(log);

        h.startStream();
        h.bytes(ByteString.toBytes("ab\r\r\ncd"), 0, 7);
        h.endStream();

        assertEquals(Arrays.asList("start", "start line", "bytes ab",
                "end line", "bytes \r", "start line", "end line", "bytes \r\n",
                "start line", "bytes cd", "end line", "end"),
                log.fetchLog());
    }

    /**
     * a single terminated line.
     */
    @Test
    public void testShortNewlineAtEnd() {
        LogHandler log = new LogHandler();
        LineHandler h = new LineHandler(log);

        h.startStream();
        h.bytes(ByteString.toBytes("a\n"), 0, 2);
        h.endStream();

        assertEquals(Arrays.asList("start", "start line", "bytes a",
                "end line", "bytes \n", "end"),
                log.fetchLog());
    }

    /**
     * two lines and a blank.
     */
    @Test
    public void testTwoLinesAndBlank() {
        LogHandler log = new LogHandler();
        LineHandler h = new LineHandler(log);

        h.startStream();
        byte[] buff = ByteString.toBytes("a\nb\n\n");
        h.bytes(buff, 0, buff.length);
        h.endStream();

        assertEquals(Arrays.asList("start", "start line", "bytes a",
                "end line", "bytes \n", "start line", "bytes b", "end line",
                "bytes \n", "start line", "end line", "bytes \n", "end"),
                log.fetchLog());
    }

}
