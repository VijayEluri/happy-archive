package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

/**
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {
    /**
     * Can read from lines from a reader.
     * 
     * @throws IOException
     */
    @Test
    public void testTwoLinesFromReader() throws IOException {
        StringReader in = new StringReader("a\nb\n");
        LineIterator lines = new LineIterator(in);

        assertTrue(lines.hasNext());
        assertEquals("a", lines.next());
        assertTrue(lines.hasNext());
        assertEquals("b", lines.next());
        assertFalse(lines.hasNext());

        lines.close();
    }

    /**
     * An empty reader has no lines.
     * 
     * @throws IOException
     */
    @Test
    public void testNoLinesFromReader() throws IOException {
        StringReader in = new StringReader("");
        LineIterator lines = new LineIterator(in);

        assertFalse(lines.hasNext());

        lines.close();
    }

    /**
     * Calling next() with no line raises an error.
     * 
     * @throws IOException
     */
    @Test(expected = NoSuchElementException.class)
    public void testNextWithNoLinesFromReader() throws IOException {
        StringReader in = new StringReader("");
        LineIterator lines = new LineIterator(in);

        lines.next();

        // the string reader is safe to not close
    }

    /**
     * Can use it in a for loop.
     */
    @Test
    public void testFor() {
        List<String> have = new ArrayList<String>();

        StringReader in = new StringReader("a\nb\n");
        LineIterator lines = new LineIterator(in);
        for (String line : lines) {
            have.add(line);
        }

        List<String> want = new ArrayList<String>();
        want.add("a");
        want.add("b");
        assertEquals(want, have);
    }
}
