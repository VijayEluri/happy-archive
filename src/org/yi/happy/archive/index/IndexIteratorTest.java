package org.yi.happy.archive.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.yi.happy.archive.BytesBuilder;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link IndexIterator}.
 */
public class IndexIteratorTest {

    /**
     * simple test
     * 
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        InputStream in = input(TestData.INDEX_MAP);
        IndexIterator it = new IndexIterator(in);

        assertTrue(it.hasNext());
        assertEquals(entry("00.dat", TestData.KEY_CONTENT_MAP), it.next());
        assertTrue(it.hasNext());
        assertEquals(entry("01.dat", TestData.KEY_CONTENT_1), it.next());
        assertTrue(it.hasNext());
        assertEquals(entry("02.dat", TestData.KEY_CONTENT_2), it.next());
        assertFalse(it.hasNext());
    }

    /**
     * It is an error to have a short line in the index.
     * 
     * @throws Exception
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testShortLine() throws Exception {
        InputStream in = input(TestData.CLEAR_CONTENT);
        IndexIterator it = new IndexIterator(in);

        it.hasNext();
    }

    /**
     * It is an error to have a key that does not parse in the index.
     * 
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadKey() throws Exception {
        InputStream in = input("0.dat\tplain\t\n");
        IndexIterator it = new IndexIterator(in);

        it.hasNext();
    }

    private IndexEntry entry(String name, TestData block) {
        String loader = "plain";
        LocatorKey key = block.getLocatorKey();
        String hash = "hash";
        String size = "size";
        return new IndexEntry(name, loader, key, hash, size);
    }

    private static InputStream input(String data) throws IOException {
        return new BytesBuilder(data).createInputStream();
    }

    private static InputStream input(TestData data) throws IOException {
        return new BytesBuilder(data.getBytes()).createInputStream();
    }
}
