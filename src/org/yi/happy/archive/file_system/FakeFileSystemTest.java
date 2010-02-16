package org.yi.happy.archive.file_system;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.ByteString;

/**
 * Tests for {@link FakeFileSystem}.
 */
public class FakeFileSystemTest {
    private FakeFileSystem fake;
    private FileSystem real;

    /**
     * setup.
     */
    @Before
    public void before() {
	fake = new FakeFileSystem();
	real = fake;
    }

    /**
     * tear down.
     */
    @After
    public void after() {
	fake = null;
	real = null;
    }

    /**
     * load a file that exists.
     * 
     * @throws IOException
     */
    @Test
    public void testLoad() throws IOException {
	real.save("test.dat", new byte[0]);

	byte[] data = real.load("test.dat");

	assertArrayEquals(new byte[0], data);
    }

    /**
     * load a file that exists and has some data.
     * 
     * @throws IOException
     */
    @Test
    public void testLoad2() throws IOException {
	real.save("test.dat", new byte[5]);

	byte[] data = real.load("test.dat");

	assertArrayEquals(new byte[5], data);
    }

    /**
     * load a file larger than the loading limit.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testLoadLimit() throws IOException {
	real.save("test.dat", ByteString.toUtf8("Hello\n"));

	real.load("test.dat", 5);
    }

    /**
     * open a file as an input stream.
     * 
     * @throws Exception
     */
    @Test
    public void testOpenInput() throws Exception {
	real.save("test.dat", ByteString.toUtf8("Hello\n"));

	InputStream in = real.openInputStream("test.dat");

	assertNotNull(in);
	byte[] data = new byte[10];
	assertEquals(6, in.read(data));
    }

    /**
     * save a file into a directory that does not exist.
     * 
     * @throws IOException
     */
    @Test(expected = FileNotFoundException.class)
    public void testNeedParentBeforeChildren() throws IOException {
	real.save("a/b", new byte[0]);
    }

    /**
     * make a directory in a directory that does not exist.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testNeedParentBeforeChildren2() throws IOException {
	real.mkdir("a/b");
    }

    /**
     * make a directory.
     * 
     * @throws IOException
     */
    @Test
    public void testMkdir() throws IOException {
	boolean out = real.mkdir("a");

	assertEquals(true, out);
    }

    /**
     * make a directory where a directory already exists.
     * 
     * @throws IOException
     */
    @Test
    public void testMkdir2() throws IOException {
	real.mkdir("a");

	boolean out = real.mkdir("a");

	assertEquals(false, out);
    }

    /**
     * make a directory where a file exists.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testMkdir3() throws IOException {
	real.save("a", new byte[0]);

	real.mkdir("a");
    }

    /**
     * make a directory as a child of a file.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testMkdir4() throws IOException {
	real.save("a", new byte[0]);

	real.mkdir("a/b");
    }

    /**
     * save a file where a directory exists.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testMkdir5() throws IOException {
	real.mkdir("a");

	real.save("a", new byte[0]);
    }

    /**
     * rename a file.
     * 
     * @throws IOException
     */
    @Test
    public void testRename() throws IOException {
	real.save("a", new byte[0]);

	real.rename("a", "b");

	assertArrayEquals(new byte[0], real.load("b"));
	assertFalse(fake.exists("a"));
    }

    /**
     * given a file and a directory; when i rename the file to the directory;
     * then I get an error.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testRename2() throws IOException {
	real.save("a", new byte[0]);
	real.mkdir("b");

	real.rename("a", "b");
    }

    /**
     * given a file and a directory; when i rename the directory to the file;
     * then I get an error.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testRename3() throws IOException {
	real.save("b", new byte[0]);
	real.mkdir("a");

	real.rename("a", "b");
    }

    /**
     * given two files; when i rename one file to the other; then the target
     * file is replaced.
     * 
     * @throws IOException
     */
    @Test
    public void testRename4() throws IOException {
	real.save("a", new byte[] { 1 });
	real.save("b", new byte[] { 2 });
	
	real.rename("a", "b");

	assertArrayEquals(new byte[] { 1 }, real.load("b"));
	assertFalse(fake.exists("a"));
    }

    /**
     * given a file; when i rename the file to a name below a directory that
     * does not exist; then i get an error.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testRename5() throws IOException {
	real.save("a", new byte[0]);

	real.rename("a", "b/a");
    }

    /**
     * given a file and a directory; when i rename the file to a name in the
     * directory; then the file is moved.
     * 
     * @throws IOException
     */
    @Test
    public void testRename6() throws IOException {
	real.save("a", new byte[0]);
	real.mkdir("b");

	real.rename("a", "b/a");

	assertArrayEquals(new byte[] {}, real.load("b/a"));
	assertFalse(fake.exists("a"));
    }

    /**
     * when I open a random output file and write bytes to it; then the bytes
     * are in the file.
     * 
     * @throws IOException
     */
    @Test
    public void randomWrite() throws IOException {
	RandomOutputFile f = real.openRandomOutputFile("a");
	f.write(new byte[] { 0, 1 });
	f.close();

	assertArrayEquals(new byte[] { 0, 1 }, real.load("a"));
    }

    /**
     * given an existing file; when I open the file as a random output file and
     * over write the start of it; then only the first part of the file is
     * changed.
     * 
     * @throws IOException
     */
    @Test
    public void randomWrite2() throws IOException {
	real.save("a", new byte[] { 1, 2, 3 });

	RandomOutputFile f = real.openRandomOutputFile("a");
	f.write(new byte[] { 0, 1 });
	f.close();

	assertArrayEquals(new byte[] { 0, 1, 3 }, real.load("a"));
    }
}
