package org.yi.happy.archive.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Iterator;

import org.junit.Test;
import org.yi.happy.archive.BytesBuilder;

/**
 * Tests for {@link TagIterator}.
 */
public class TagIteratorTest {
    /**
     * Check the basic usage.
     * 
     * @throws Exception
     */
    @Test
    public void testBasicUsage() throws Exception {
        InputStream in = input("a=b\n" + "\n" + "b=c\n" + "\n");

        TagIterator it = new TagIterator(in);

        Iterator<Tag> i = it.iterator();
        assertTrue(i.hasNext());
        assertEquals(tag("a", "b"), i.next());
        assertTrue(i.hasNext());
        assertEquals(tag("b", "c"), i.next());
        assertFalse(i.hasNext());
    }

    /**
     * check with extra blank line.
     */
    @Test
    public void testBlankLine() {
        InputStream in = input("a=b\n" + "\n" + "\n" + "b=c\n" + "\n");

        TagIterator it = new TagIterator(in);

        Iterator<Tag> i = it.iterator();
        assertEquals(tag("a", "b"), i.next());
        assertEquals(tag("b", "c"), i.next());
        assertFalse(i.hasNext());
    }

    /**
     * check with extra blank line at end.
     */
    @Test
    public void testBlankLineAtEnd() {
        InputStream in = input("a=b\n" + "\n" + "b=c\n" + "\n" + "\n");

        TagIterator it = new TagIterator(in);

        Iterator<Tag> i = it.iterator();
        assertEquals(tag("a", "b"), i.next());
        assertEquals(tag("b", "c"), i.next());
        assertFalse(i.hasNext());
    }

    /**
     * check with no value.
     */
    @Test
    public void testNoValue() {
        InputStream in = input("a\n" + "\n" + "b=c\n" + "\n");

        TagIterator it = new TagIterator(in);

        Iterator<Tag> i = it.iterator();
        assertEquals(tag("a", ""), i.next());
        assertEquals(tag("b", "c"), i.next());
        assertFalse(i.hasNext());
    }

    /**
     * check with two fields value.
     */
    @Test
    public void testTwoFields() {
        InputStream in = input("a=b\n" + "b=c\n" + "\n");

        TagIterator it = new TagIterator(in);

        Iterator<Tag> i = it.iterator();
        assertEquals(tag("a", "b", "b", "c"), i.next());
    }

    private static Tag tag(String... parts) {
        TagBuilder tag = new TagBuilder();
        for (int i = 0; i < parts.length; i += 2) {
            tag.add(parts[i], parts[i + 1]);
        }
        return tag.create();
    }

    private static InputStream input(String data) {
        return new BytesBuilder(data).createInputStream();
    }
}
