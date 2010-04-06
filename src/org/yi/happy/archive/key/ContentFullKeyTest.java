package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * tests for ContentFullKey
 */
public class ContentFullKeyTest {
    /**
     * create a good key
     */
    @Test
    public void testGood() {
        HashValue hash = new HashValue(0x00);
        PassValue pass = new PassValue(0x11);

        ContentFullKey key = new ContentFullKey(hash, pass);

        assertEquals("content-hash:00:11", key.toString());
    }

    /**
     * create a key with an empty pass
     */
    @Test
    public void testGood2() {
        HashValue hash = new HashValue(0x00);
        PassValue pass = new PassValue();

        new ContentFullKey(hash, pass);
    }

    /**
     * convert a content key to a locator key
     */
    @Test
    public void testToLocatorKey1() {
        ContentFullKey in = new ContentFullKey(new HashValue(0x00, 0x11, 0x22),
                new PassValue(0x33, 0x44, 0x55));

        Key have = in.toLocatorKey();

        Key want = new ContentLocatorKey(new HashValue(0x00, 0x11, 0x22));
        assertEquals(want, have);
    }
}
