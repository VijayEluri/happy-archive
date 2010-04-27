package org.yi.happy.archive.block.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * tests for {@link Range}.
 */
public class RangeTest {
    /**
     * test (5 .. 15) before (10 .. 20).
     */
    @Test
    public void testBefore1() {
        Range a = new Range(5, 10);
        Range b = new Range(10, 10);

        assertEquals(new Range(5, 5), a.before(b));
    }

    /**
     * test (5 .. 15) before (15 .. 25).
     */
    @Test
    public void testBefore2() {
        Range a = new Range(5, 10);
        Range b = new Range(15, 10);

        assertEquals(new Range(5, 10), a.before(b));
    }

    /**
     * test (15 .. 25) before (5 .. 15).
     */
    @Test
    public void testBefore3() {
        Range a = new Range(15, 10);
        Range b = new Range(5, 10);

        assertEquals(new Range(5, 0), a.before(b));
    }

    /**
     * test (5 .. 15) before (6 .. 16).
     */
    @Test
    public void testBefore4() {
        Range a = new Range(5, 10);
        Range b = new Range(6, 10);

        assertEquals(new Range(5, 1), a.before(b));
    }

    /**
     * test (5 .. 15) after (5 .. 15).
     */
    @Test
    public void testAfter1() {
        Range a = new Range(5, 10);
        Range b = new Range(5, 10);

        assertEquals(new Range(15, 0), a.after(b));
    }

    /**
     * test (5 .. 15) after (10 .. 11).
     */
    @Test
    public void testAfter2() {
        Range a = new Range(5, 10);
        Range b = new Range(10, 1);

        assertEquals(new Range(11, 4), a.after(b));
    }

    /**
     * test (0 .. 10) after (5 .. 15)
     */
    @Test
    public void testAfter3() {
        Range a = new Range(0, 10);
        Range b = new Range(5, 10);

        assertEquals(new Range(15, 0), a.after(b));
    }

    /**
     * test (10 .. 20) after (5 .. 15)
     */
    @Test
    public void testAfter4() {
        Range a = new Range(10, 10);
        Range b = new Range(5, 10);

        assertEquals(new Range(15, 5), a.after(b));
    }

    /**
     * (10 .. 20) > (5 .. 15)
     */
    @Test
    public void testCompare1() {
        Range a = new Range(10, 10);
        Range b = new Range(5, 10);

        assertTrue(a.compareTo(b) > 0);
    }

    /**
     * (5 .. 10) < (5 .. 15)
     */
    @Test
    public void testCompare2() {
        Range a = new Range(5, 5);
        Range b = new Range(5, 10);

        assertTrue(a.compareTo(b) < 0);
    }
}
