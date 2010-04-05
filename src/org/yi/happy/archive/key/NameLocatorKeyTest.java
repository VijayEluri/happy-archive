package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link NameLocatorKey}
 */
public class NameLocatorKeyTest {
    /**
     * a good locator
     */
    @Test
    public void testGood2() {
        Bytes hash = new Bytes(0x00);
        LocatorKey key = new NameLocatorKey(hash);
        assertEquals("name-hash", key.getType());
        assertEquals(hash, key.getHash());
    }

    /**
     * a bad locator
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad1() {
        Bytes hash = new Bytes();
        new NameLocatorKey(hash);
    }

    /**
     * convert a name locator key to a locator key
     */
    @Test
    public void testToLocatorKey3() {
        Key key = TestData.KEY_NAME.getLocatorKey();
        assertTrue(key instanceof NameLocatorKey);

        LocatorKey have = key.toLocatorKey();
        LocatorKey want = TestData.KEY_NAME.getLocatorKey();
        assertEquals(want, have);
    }
}
