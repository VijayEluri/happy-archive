package org.yi.happy.archive.file_system;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;



public class FakeRandomOutputFileTest {
    @Test
    public void testWrite() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();

	f.write(new byte[] { 0, 1, 2, 3 });

	f.close();

	assertArrayEquals(new byte[] { 0, 1, 2, 3 }, f.getBytes());
    }

    @Test
    public void testClose() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();

	f.close();

	assertArrayEquals(new byte[] {}, f.getBytes());
    }

    @Test
    public void testWriteBackward() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();

	f.setPosition(3);
	f.write(3);

	f.setPosition(1);
	f.write(new byte[] { 0, 1, 2, 3 }, 1, 2);

	f.setPosition(0);
	f.write(0);

	f.close();

	assertArrayEquals(new byte[] { 0, 1, 2, 3 }, f.getBytes());
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();
	f.close();

	f.write(0);
    }

    @Test(expected = IOException.class)
    public void testSeekAfterClose() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();
	f.close();

	f.setPosition(0);
    }

    @Test(expected = IOException.class)
    public void testTellAfterClose() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();
	f.close();

	f.getPosition();
    }

    @Test
    public void testCloseAfterClose() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();
	f.close();

	f.close();
    }

    @Test
    public void testModify() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile(new byte[] { 0, 1, 2,
		3 });

	f.write(4);

	f.close();

	assertArrayEquals(new byte[] { 4, 1, 2, 3 }, f.getBytes());
    }

    @Test
    public void testSetPosition() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();

	f.setPosition(5);

	assertEquals(5, f.getPosition());
    }
}
