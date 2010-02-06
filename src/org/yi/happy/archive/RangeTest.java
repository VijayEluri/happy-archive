package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class RangeTest {
    @Test
    public void testBefore1() {
	Range a = new Range(5, 10);
	Range b = new Range(10, 10);

	assertEquals(new Range(5, 5), a.before(b));
    }

    @Test
    public void testBefore2() {
	Range a = new Range(5, 10);
	Range b = new Range(15, 10);

	assertEquals(new Range(5, 10), a.before(b));
    }

    @Test
    public void testBefore3() {
	Range a = new Range(15, 10);
	Range b = new Range(5, 10);

	assertEquals(new Range(5, 0), a.before(b));
    }

    @Test
    public void testBefore4() {
	Range a = new Range(5, 10);
	Range b = new Range(6, 10);

	assertEquals(new Range(5, 1), a.before(b));
    }

    @Test
    public void testAfter1() {
	Range a = new Range(5, 10);
	Range b = new Range(5, 10);

	assertEquals(new Range(15, 0), a.after(b));
    }

    @Test
    public void testAfter2() {
	Range a = new Range(5, 10);
	Range b = new Range(10, 1);

	assertEquals(new Range(11, 4), a.after(b));
    }

    @Test
    public void testAfter3() {
	Range a = new Range(0, 10);
	Range b = new Range(5, 10);

	assertEquals(new Range(15, 0), a.after(b));
    }

    @Test
    public void testAfter4() {
	Range a = new Range(10, 10);
	Range b = new Range(5, 10);

	assertEquals(new Range(15, 5), a.after(b));
    }

}
