package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.annotate.SmellsMessy;

/**
 * Tests for {@link FileStoreMemory}.
 */
@SmellsMessy(/* TODO use some builders */)
public class FileStoreMemoryTest {
    private FileStoreMemory fake;
    private FileStore real;

    /**
     * setup.
     */
    @Before
    public void before() {
        fake = new FileStoreMemory();
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
        real.put("test.dat", new byte[0]);

        byte[] data = real.get("test.dat");

        assertArrayEquals(new byte[0], data);
    }

    /**
     * load a file that exists and has some data.
     * 
     * @throws IOException
     */
    @Test
    public void testLoad2() throws IOException {
        real.put("test.dat", new byte[5]);

        byte[] data = real.get("test.dat");

        assertArrayEquals(new byte[5], data);
    }

    /**
     * load a file larger than the loading limit.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testLoadLimit() throws IOException {
        real.put("test.dat", ByteString.toUtf8("Hello\n"));

        real.get("test.dat", 5);
    }

    /**
     * open a file as an input stream.
     * 
     * @throws Exception
     */
    @Test
    public void testOpenInput() throws Exception {
        real.put("test.dat", ByteString.toUtf8("Hello\n"));

        InputStream in = real.getStream("test.dat");

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
        real.put("a/b", new byte[0]);
    }

    /**
     * make a directory in a directory that does not exist.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testNeedParentBeforeChildren2() throws IOException {
        real.putDir("a/b");
    }

    /**
     * make a directory.
     * 
     * @throws IOException
     */
    @Test
    public void testMkdir() throws IOException {
        boolean out = real.putDir("a");

        assertEquals(true, out);
    }

    /**
     * make a directory where a directory already exists.
     * 
     * @throws IOException
     */
    @Test
    public void testMkdir2() throws IOException {
        real.putDir("a");

        boolean out = real.putDir("a");

        assertEquals(false, out);
    }

    /**
     * make a directory where a file exists.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testMkdir3() throws IOException {
        real.put("a", new byte[0]);

        real.putDir("a");
    }

    /**
     * make a directory as a child of a file.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testMkdir4() throws IOException {
        real.put("a", new byte[0]);

        real.putDir("a/b");
    }

    /**
     * save a file where a directory exists.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testMkdir5() throws IOException {
        real.putDir("a");

        real.put("a", new byte[0]);
    }

    /**
     * List the current directory with one item.
     * 
     * @throws IOException
     */
    @Test
    public void testList1() throws IOException {
        real.putDir("a");

        List<String> have = real.list("");

        assertEquals(Arrays.asList("a"), have);
    }

    /**
     * List a subdirectory current directory with one item.
     * 
     * @throws IOException
     */
    @Test
    public void testList2() throws IOException {
        real.putDir("a");
        real.putDir("a/b");

        List<String> have = real.list("a");

        assertEquals(Arrays.asList("b"), have);
    }

    /**
     * Given a file, when I list children of the file, then an error is raised.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void listOnFile() throws IOException {
        real.put("a", new byte[0]);

        real.list("a");
    }

    /**
     * create an absolute directory.
     * 
     * @throws IOException
     */
    @Test
    public void testMkdirAbsolute() throws IOException {
        real.putDir("/a");

        assertTrue(real.isDir("/a"));
    }

    /**
     * directory is a directory
     * 
     * @throws IOException
     */
    @Test
    public void testIsDir1() throws IOException {
        real.putDir("a");

        assertTrue(real.isDir("a"));
    }

    /**
     * file is not a directory
     * 
     * @throws IOException
     */
    @Test
    public void testIsDir2() throws IOException {
        real.put("a", new byte[0]);

        assertFalse(real.isDir("a"));
    }

    /**
     * not exist is not a directory
     * 
     * @throws IOException
     */
    @Test
    public void testIsDir3() throws IOException {
        assertFalse(real.isDir("a"));
    }

    /**
     * directory is not a file
     * 
     * @throws IOException
     */
    @Test
    public void testIsFile1() throws IOException {
        real.putDir("a");

        assertFalse(real.isFile("a"));
    }

    /**
     * file is a file
     * 
     * @throws IOException
     */
    @Test
    public void testIsFile2() throws IOException {
        real.put("a", new byte[0]);

        assertTrue(real.isFile("a"));
    }

    /**
     * not exist is not a file
     * 
     * @throws IOException
     */
    @Test
    public void testIsFile3() throws IOException {
        assertFalse(real.isFile("a"));
    }
}
