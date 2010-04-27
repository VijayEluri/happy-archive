package org.yi.happy.archive.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * tests for {@link LongRange}.
 */
public class LongRangeTest {
    /**
     * test (5 .. 15) before (10 .. 20).
     */
    @Test
    public void testBefore1() {
        LongRange a = new LongRange(5, 10);
        LongRange b = new LongRange(10, 10);

        assertEquals(new LongRange(5, 5), a.before(b));
    }

    /**
     * test (5 .. 15) before (15 .. 25).
     */
    @Test
    public void testBefore2() {
        LongRange a = new LongRange(5, 10);
        LongRange b = new LongRange(15, 10);

        assertEquals(new LongRange(5, 10), a.before(b));
    }

    /**
     * test (15 .. 25) before (5 .. 15).
     */
    @Test
    public void testBefore3() {
        LongRange a = new LongRange(15, 10);
        LongRange b = new LongRange(5, 10);

        assertEquals(new LongRange(5, 0), a.before(b));
    }

    /**
     * test (5 .. 15) before (6 .. 16).
     */
    @Test
    public void testBefore4() {
        LongRange a = new LongRange(5, 10);
        LongRange b = new LongRange(6, 10);

        assertEquals(new LongRange(5, 1), a.before(b));
    }

    /**
     * test (5 .. 15) after (5 .. 15).
     */
    @Test
    public void testAfter1() {
        LongRange a = new LongRange(5, 10);
        LongRange b = new LongRange(5, 10);

        assertEquals(new LongRange(15, 0), a.after(b));
    }

    /**
     * test (5 .. 15) after (10 .. 11).
     */
    @Test
    public void testAfter2() {
        LongRange a = new LongRange(5, 10);
        LongRange b = new LongRange(10, 1);

        assertEquals(new LongRange(11, 4), a.after(b));
    }

    /**
     * test (0 .. 10) after (5 .. 15)
     */
    @Test
    public void testAfter3() {
        LongRange a = new LongRange(0, 10);
        LongRange b = new LongRange(5, 10);

        assertEquals(new LongRange(15, 0), a.after(b));
    }

    /**
     * test (10 .. 20) after (5 .. 15)
     */
    @Test
    public void testAfter4() {
        LongRange a = new LongRange(10, 10);
        LongRange b = new LongRange(5, 10);

        assertEquals(new LongRange(15, 5), a.after(b));
    }

    /**
     * (10 .. 20) > (5 .. 15)
     */
    @Test
    public void testCompare1() {
        LongRange a = new LongRange(10, 10);
        LongRange b = new LongRange(5, 10);

        assertTrue(a.compareTo(b) > 0);
    }

    /**
     * (5 .. 10) < (5 .. 15)
     */
    @Test
    public void testCompare2() {
        LongRange a = new LongRange(5, 5);
        LongRange b = new LongRange(5, 10);

        assertTrue(a.compareTo(b) < 0);
    }

    /**
     * (5 .. 6) < (6 .. 6)
     */
    @Test
    public void testCompare3() {
        LongRange a = new LongRange(5, 1);
        LongRange b = new LongRange(6, 0);

        assertTrue(a.compareTo(b) < 0);
    }

    /**
     * (5 .. 6) > (5 .. 5)
     */
    @Test
    public void testCompare4() {
        LongRange a = new LongRange(5, 1);
        LongRange b = new LongRange(5, 0);

        assertTrue(a.compareTo(b) > 0);
    }
}
