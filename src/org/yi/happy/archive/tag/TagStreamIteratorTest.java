package org.yi.happy.archive.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Iterator;

import org.junit.Test;
import org.yi.happy.archive.BytesBuilder;

/**
 * Tests for {@link TagStreamIterator}.
 */
public class TagStreamIteratorTest {
    /**
     * Check the basic usage.
     * 
     * @throws Exception
     */
    @Test
    public void testBasicUsage() throws Exception {
        InputStream in = new BytesBuilder("a=b\n" + "\n" + "b=c\n" + "\n")
                .createInputStream();

        TagStreamIterator it = new TagStreamIterator(in);

        Iterator<Tag> i = it.iterator();
        assertTrue(i.hasNext());
        assertEquals(new TagBuilder().add("a", "b").create(), i.next());
        assertTrue(i.hasNext());
        assertEquals(new TagBuilder().add("b", "c").create(), i.next());
        assertFalse(i.hasNext());
    }
}
