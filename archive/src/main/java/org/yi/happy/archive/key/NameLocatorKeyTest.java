package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
}
