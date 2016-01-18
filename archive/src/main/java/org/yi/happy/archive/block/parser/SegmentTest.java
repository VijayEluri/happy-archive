package org.yi.happy.archive.block.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * tests for {@link Segment}.
 */
public class SegmentTest {
    /**
     * (5 + 10 = 15) before (10 + 10 = 20) == (5 + 5 = 10)
     */
    @Test
    public void testBefore1() {
        Segment a = new Segment(5, 10);
        Segment b = new Segment(10, 10);

        assertEquals(new Segment(5, 5), a.before(b));
    }

    /**
     * (5 + 10 = 15) before (15 + 10 = 25) == (5 + 10 = 15)
     */
    @Test
    public void testBefore2() {
        Segment a = new Segment(5, 10);
        Segment b = new Segment(15, 10);

        assertEquals(new Segment(5, 10), a.before(b));
    }

    /**
     * (15 + 10 = 25) before (5 + 10 = 15) == (5 + 0 = 5)
     */
    @Test
    public void testBefore3() {
        Segment a = new Segment(15, 10);
        Segment b = new Segment(5, 10);

        assertEquals(new Segment(5, 0), a.before(b));
    }

    /**
     * (5 + 10 = 15) before (6 + 10 = 16) == (5 + 1 = 6).
     */
    @Test
    public void testBefore4() {
        Segment a = new Segment(5, 10);
        Segment b = new Segment(6, 10);

        assertEquals(new Segment(5, 1), a.before(b));
    }

    /**
     * (5 + 10 = 15) after (5 + 10 = 15) == (15 + 0 = 15).
     */
    @Test
    public void testAfter1() {
        Segment a = new Segment(5, 10);
        Segment b = new Segment(5, 10);

        assertEquals(new Segment(15, 0), a.after(b));
    }

    /**
     * (5 + 10 = 15) after (10 + 1 = 11) == (11 + 4 = 15)
     */
    @Test
    public void testAfter2() {
        Segment a = new Segment(5, 10);
        Segment b = new Segment(10, 1);

        assertEquals(new Segment(11, 4), a.after(b));
    }

    /**
     * (0 + 10 = 10) after (5 + 10 = 15) == (15 + 0 = 15)
     */
    @Test
    public void testAfter3() {
        Segment a = new Segment(0, 10);
        Segment b = new Segment(5, 10);

        assertEquals(new Segment(15, 0), a.after(b));
    }

    /**
     * (10 + 10 = 20) after (5 + 10 = 15) == (15 + 5 = 20)
     */
    @Test
    public void testAfter4() {
        Segment a = new Segment(10, 10);
        Segment b = new Segment(5, 10);

        assertEquals(new Segment(15, 5), a.after(b));
    }
}
