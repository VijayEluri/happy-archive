package org.yi.happy.archive.file_system;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.ByteString;

public class FakeFileSystemTest {
    private FakeFileSystem fake;
    private FileSystem real;

    @Before
    public void before() {
	fake = new FakeFileSystem();
	real = fake;
    }

    public void after() {
	fake = null;
	real = null;
    }

    @Test
    public void testLoad() throws IOException {
	real.save("test.dat", new byte[0]);

	byte[] data = real.load("test.dat");

	assertArrayEquals(new byte[0], data);
    }

    @Test
    public void testLoad2() throws IOException {
	real.save("test.dat", new byte[5]);

	byte[] data = real.load("test.dat");

	assertArrayEquals(new byte[5], data);
    }

    @Test(expected = IOException.class)
    public void testLoadLimit() throws IOException {
	real.save("test.dat", ByteString.toUtf8("Hello\n"));

	real.load("test.dat", 5);
    }

    @Test
    public void testOpenInput() throws Exception {
	real.save("test.dat", ByteString.toUtf8("Hello\n"));

	InputStream in = real.openInputStream("test.dat");

	assertNotNull(in);
	byte[] data = new byte[10];
	assertEquals(6, in.read(data));
    }

    @Test(expected = FileNotFoundException.class)
    public void testNeedParentBeforeChildren() throws IOException {
	real.save("a/b", new byte[0]);
    }

    @Test(expected = IOException.class)
    public void testNeedParentBeforeChildren2() throws IOException {
	real.mkdir("a/b");
    }

    @Test
    public void testMkdir() throws IOException {
	boolean out = real.mkdir("a");

	assertEquals(true, out);
    }

    @Test
    public void testMkdir2() throws IOException {
	real.mkdir("a");

	boolean out = real.mkdir("a");

	assertEquals(false, out);
    }

    @Test(expected = IOException.class)
    public void testMkdir3() throws IOException {
	real.save("a", new byte[0]);

	real.mkdir("a");
    }

    @Test(expected = IOException.class)
    public void testMkdir4() throws IOException {
	real.save("a", new byte[0]);

	real.mkdir("a/b");
    }

    @Test(expected = IOException.class)
    public void testMkdir5() throws IOException {
	real.mkdir("a");

	real.save("a", new byte[0]);
    }

    @Test
    public void testRename() throws IOException {
	real.save("a", new byte[0]);

	real.rename("a", "b");

	assertArrayEquals(new byte[0], real.load("b"));
	assertFalse(fake.exists("a"));
    }

    @Test(expected = IOException.class)
    public void testRename2() throws IOException {
	real.save("a", new byte[0]);
	real.mkdir("b");

	real.rename("a", "b");
    }

    @Test(expected = IOException.class)
    public void testRename3() throws IOException {
	real.save("b", new byte[0]);
	real.mkdir("a");

	real.rename("a", "b");
    }

    @Test
    public void testRename4() throws IOException {
	real.save("a", new byte[] { 1 });
	real.save("b", new byte[] { 2 });
	
	real.rename("a", "b");

	assertArrayEquals(new byte[] { 1 }, real.load("b"));
	assertFalse(fake.exists("a"));
    }

    @Test(expected = IOException.class)
    public void testRename5() throws IOException {
	real.save("a", new byte[0]);

	real.rename("a", "b/a");
    }

    @Test
    public void testRename6() throws IOException {
	real.save("a", new byte[0]);
	real.mkdir("b");

	real.rename("a", "b/a");

	assertArrayEquals(new byte[] {}, real.load("b/a"));
	assertFalse(fake.exists("a"));
    }

    @Test
    public void randomWrite() throws IOException {
	RandomOutputFile f = real.openRandomOutputFile("a");
	f.write(new byte[] { 0, 1 });
	f.close();

	assertArrayEquals(new byte[] { 0, 1 }, real.load("a"));
    }

    @Test
    public void randomWrite2() throws IOException {
	real.save("a", new byte[] { 1, 2, 3 });

	RandomOutputFile f = real.openRandomOutputFile("a");
	f.write(new byte[] { 0, 1 });
	f.close();

	assertArrayEquals(new byte[] { 0, 1, 3 }, real.load("a"));
    }
}
