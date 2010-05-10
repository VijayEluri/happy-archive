package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for ContentLocatorKey
 */
public class ContentLocatorKeyTest {
    /**
     * a good locator
     */
    @Test
    public void testGood1() {
        HashValue hash = new HashValue(0);
        LocatorKey key = new ContentLocatorKey(hash);
        assertEquals("content-hash", key.getType());
        assertEquals(hash, key.getHash());
        assertEquals("content-hash:00", key.toString());
    }

    /**
     * convert a content locator key to a locator key
     */
    @Test
    public void testToLocatorKey5() {
        LocatorKey key = TestData.KEY_CONTENT.getLocatorKey();

        assertTrue(key instanceof ContentLocatorKey);

        LocatorKey have = key.toLocatorKey();

        LocatorKey want = TestData.KEY_CONTENT.getLocatorKey();
        assertEquals(want, have);
    }

}
