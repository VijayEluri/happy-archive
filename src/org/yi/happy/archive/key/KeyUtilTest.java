package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests on ParseKey
 */
public class KeyUtilTest {
    /**
     * convert a content key to a locator key
     */
    @Test
    public void testToLocatorKey1() {
        Key in = new ContentFullKey(new byte[] { 0x00, 0x11, 0x22 },
                new byte[] { 0x33, 0x44, 0x55 });

        Key have = KeyUtil.toLocatorKey(in);

        Key want = new ContentLocatorKey(new byte[] { 0x00, 0x11, 0x22 });
        assertEquals(want, have);
    }

    /**
     * convert a full name key to a locator key
     */
    @Test
    public void testToLocatorKey2() {
		Key key = TestData.KEY_NAME.getFullKey();
        assertTrue(key instanceof NameFullKey);

        LocatorKey have = KeyUtil.toLocatorKey(key);
		LocatorKey want = TestData.KEY_NAME.getLocatorKey();
        assertEquals(want, have);
    }

    /**
     * convert a name locator key to a locator key
     */
    @Test
    public void testToLocatorKey3() {
		Key key = TestData.KEY_NAME.getLocatorKey();
        assertTrue(key instanceof NameLocatorKey);

        LocatorKey have = KeyUtil.toLocatorKey(key);
		LocatorKey want = TestData.KEY_NAME.getLocatorKey();
        assertEquals(want, have);
    }

    /**
     * convert a full content key to a locator key
     */
    @Test
    public void testToLocatorKey4() {
		Key key = TestData.KEY_CONTENT.getFullKey();
        assertTrue(key instanceof ContentFullKey);

        LocatorKey have = KeyUtil.toLocatorKey(key);
		LocatorKey want = TestData.KEY_CONTENT.getLocatorKey();
        assertEquals(want, have);
    }

    /**
     * convert a content locator key to a locator key
     */
    @Test
    public void testToLocatorKey5() {
		Key key = TestData.KEY_CONTENT.getLocatorKey();
        assertTrue(key instanceof ContentLocatorKey);

        LocatorKey have = KeyUtil.toLocatorKey(key);
		LocatorKey want = TestData.KEY_CONTENT.getLocatorKey();
        assertEquals(want, have);
    }

    /**
     * convert null to a locator key
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocatorKey6() {
        KeyUtil.toLocatorKey(null);
    }
}
