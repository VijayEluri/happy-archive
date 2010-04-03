package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

/**
 * Tests for {@link LineCursor}.
 */
public class LineCursorTest {
    /**
     * check the normal good case.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
	Reader in = new StringReader("a\nb\n");

	LineCursor c = new LineCursor(in);

	assertTrue(c.next());
	assertEquals("a", c.get());
	assertTrue(c.next());
	assertEquals("b", c.get());
	assertFalse(c.next());

	c.close();
    }

    /**
     * check with an empty stream.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
	Reader in = new StringReader("");

	LineCursor c = new LineCursor(in);

	assertFalse(c.next());

	c.close();
    }

    /**
     * check with no line to fetch.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalStateException.class)
    public void test3() throws IOException {
	Reader in = new StringReader("");

	LineCursor c = new LineCursor(in);

	c.get();
    }

    /**
     * check fetching before the first line.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalStateException.class)
    public void test4() throws IOException {
	Reader in = new StringReader("a\nb\n");

	LineCursor c = new LineCursor(in);

	c.get();
    }

    /**
     * check fetch after the last line.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalStateException.class)
    public void test5() throws IOException {
	Reader in = new StringReader("a\nb\n");
	LineCursor c = new LineCursor(in);
	assertTrue(c.next());
	assertEquals("a", c.get());
	assertTrue(c.next());
	assertEquals("b", c.get());
	assertFalse(c.next());

	c.get();
    }
}
