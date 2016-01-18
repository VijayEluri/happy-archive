package org.yi.happy.archive.key;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests between {@link LocatorKey} classes.
 */
public class LocatorKeyTest {
    /**
     * keys are sorted type first.
     */
    @Test
    public void sort1() {
        LocatorKey a = new NameLocatorKey(new HashValue(0));
        LocatorKey b = new ContentLocatorKey(new HashValue(1));

        assertTrue(a.compareTo(b) > 0);
    }

    /**
     * keys are sorted type first.
     */
    @Test
    public void sort2() {
        LocatorKey a = new NameLocatorKey(new HashValue(0));
        LocatorKey b = new ContentLocatorKey(new HashValue(0));

        assertTrue(a.compareTo(b) > 0);
    }
}
