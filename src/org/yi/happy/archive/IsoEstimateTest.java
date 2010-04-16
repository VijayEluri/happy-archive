package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link IsoEstimate}.
 */
public class IsoEstimateTest {
    /**
     * Check the size of a single file image.
     */
    @Test
    public void test1() {
        IsoEstimate e = new IsoEstimate();

        e.add(1024);

        assertEquals(1, e.getCount());
        assertEquals(1, e.getDataSectors());
        assertEquals(175, e.getTotalSectors());
        assertEquals(2048, e.getSectorSize());
        assertEquals(175 * 2048l, e.getSize());
    }

    /**
     * Check the size of a 1000 files of one sector each image.
     */
    @Test
    public void test2() {
        IsoEstimate e = new IsoEstimate();

        for (int i = 0; i < 1000; i++) {
            e.add(2048);
        }

        assertEquals(1000, e.getCount());
        assertEquals(1000, e.getDataSectors());
        assertEquals(1197, e.getTotalSectors());
        assertEquals(2048, e.getSectorSize());
        assertEquals(1197 * 2048l, e.getSize());
    }

    /**
     * Check the size of a 4000 files of 1 MiB each image.
     */
    @Test
    public void test3() {
        IsoEstimate e = new IsoEstimate();

        for (int i = 0; i < 4000; i++) {
            e.add(1024 * 1024);
        }

        assertEquals(4000, e.getCount());
        assertEquals(2048000, e.getDataSectors());
        assertEquals(2048269, e.getTotalSectors());
        assertEquals(2048, e.getSectorSize());
        assertEquals(4194854912l, e.getSize());
    }

    /**
     * Check the size of a 4000 files of 1 MiB each image.
     */
    @Test
    public void testRemove() {
        IsoEstimate e = new IsoEstimate();

        for (int i = 0; i < 4000; i++) {
            e.add(1024 * 1024);
        }

        e.remove(1024 * 1024);

        assertEquals(3999, e.getCount());
        assertEquals(2047488, e.getDataSectors());
        assertEquals(2047757, e.getTotalSectors());
        assertEquals(2048, e.getSectorSize());
        assertEquals(4193806336l, e.getSize());
    }

    @Test
    public void testMegaSize1() {
        IsoEstimate e = new IsoEstimate();

        assertEquals(1, e.getMegaSize());
    }

    @Test
    public void testMegaSize2() {
        IsoEstimate e = new IsoEstimate();

        for (int i = 0; i < 4000; i++) {
            e.add(1024 * 1024);
        }

        assertEquals(4195, e.getMegaSize());
    }
    /*
     * with rock
     * 
     * 4000 x 1MiB = 4194930688
     */

}
