package org.yi.happy.archive.file_system;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

/**
 * Tests for {@link FakeRandomOutputFile}.
 */
public class FakeRandomOutputFileTest {
    /**
     * write at the beginning.
     * 
     * @throws IOException
     */
    @Test
    public void testWrite() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();

	f.write(new byte[] { 0, 1, 2, 3 });

	f.close();

	assertArrayEquals(new byte[] { 0, 1, 2, 3 }, f.getBytes());
    }

    /**
     * close when empty.
     * 
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();

	f.close();

	assertArrayEquals(new byte[] {}, f.getBytes());
    }

    /**
     * write the content backwards.
     * 
     * @throws IOException
     */
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

    /**
     * write after closing the file.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();
	f.close();

	f.write(0);
    }

    /**
     * seek after closing the file.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testSeekAfterClose() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();
	f.close();

	f.setPosition(0);
    }

    /**
     * get the file position after closing the file.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testTellAfterClose() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();
	f.close();

	f.getPosition();
    }

    /**
     * close the file twice.
     * 
     * @throws IOException
     */
    @Test
    public void testCloseAfterClose() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();
	f.close();

	f.close();
    }

    /**
     * overwrite the first part of a file.
     * 
     * @throws IOException
     */
    @Test
    public void testModify() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile(new byte[] { 0, 1, 2,
		3 });

	f.write(4);

	f.close();

	assertArrayEquals(new byte[] { 4, 1, 2, 3 }, f.getBytes());
    }

    /**
     * seek in the file.
     * 
     * @throws IOException
     */
    @Test
    public void testSetPosition() throws IOException {
	FakeRandomOutputFile f = new FakeRandomOutputFile();

	f.setPosition(5);

	assertEquals(5, f.getPosition());
    }
}
