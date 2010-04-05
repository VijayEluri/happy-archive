package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
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
        HashValue hash = new HashValue(0x00);
        LocatorKey key = new NameLocatorKey(hash);
        assertEquals("name-hash", key.getType());
        assertEquals(hash, key.getHash());
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
