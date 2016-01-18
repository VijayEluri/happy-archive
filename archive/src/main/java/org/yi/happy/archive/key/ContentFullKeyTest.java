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
    public void testString() {
        String have = key_001122_334455().toString();

        assertEquals("content-hash:001122:334455", have);
    }

    /**
     * convert a content key to a locator key
     */
    @Test
    public void testToLocatorKey1() {
        LocatorKey have = key_001122_334455().toLocatorKey();

        assertEquals(new ContentLocatorKey(hash_001122()), have);
    }

    private ContentFullKey key_001122_334455() {
        return new ContentFullKey(hash_001122(), pass_334455());
    }

    private PassValue pass_334455() {
        return new PassValue(0x33, 0x44, 0x55);
    }

    private HashValue hash_001122() {
        return new HashValue(0x00, 0x11, 0x22);
    }
}
