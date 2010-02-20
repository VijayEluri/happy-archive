package org.yi.happy.archive.tag;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.yi.happy.archive.SimpleRetrieveBlock;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link RestoreManager}.
 */
public class RestoreManagerTest {
    /**
     * manage the restoration of one file, with the blocks not initially
     * available.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
	SimpleRetrieveBlock store = new SimpleRetrieveBlock();
	FileSystem fs = new FakeFileSystem();
	RestoreManager r = new RestoreManager(fs, store);

	r.addFile("test.dat", TestData.KEY_CONTENT.getFullKey());

	r.step();

	assertEquals(0, r.getProgress());
	assertEquals(Arrays.asList(TestData.KEY_CONTENT.getFullKey()), r
		.getPending());
	assertEquals(false, r.isDone());

	store.put(TestData.KEY_CONTENT);

	r.step();

	assertEquals(1, r.getProgress());
	assertEquals(Arrays.asList(), r.getPending());
	assertEquals(true, r.isDone());

	assertArrayEquals(TestData.FILE_CONTENT.getBytes(), fs.load("test.dat"));
    }

    /**
     * manage the restoration of two files, with the blocks not initially
     * available, and becoming available at the same time.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
	SimpleRetrieveBlock store = new SimpleRetrieveBlock();
	FileSystem fs = new FakeFileSystem();
	RestoreManager r = new RestoreManager(fs, store);

	r.addFile("test.dat", TestData.KEY_CONTENT.getFullKey());
	r.addFile("hello.dat", TestData.KEY_CONTENT_40.getFullKey());

	r.step();

	assertEquals(0, r.getProgress());
	assertEquals(Arrays.asList(TestData.KEY_CONTENT.getFullKey(),
		TestData.KEY_CONTENT_40.getFullKey()), r.getPending());
	assertEquals(false, r.isDone());

	store.put(TestData.KEY_CONTENT);
	store.put(TestData.KEY_CONTENT_40);

	r.step();

	assertEquals(2, r.getProgress());
	assertEquals(Arrays.asList(), r.getPending());
	assertEquals(true, r.isDone());

	assertArrayEquals(TestData.FILE_CONTENT.getBytes(), fs.load("test.dat"));
	assertArrayEquals(TestData.FILE_CONTENT_40.getBytes(), fs
		.load("hello.dat"));
    }

    /**
     * restore two files of the same data.
     * 
     * @throws IOException
     */
    @Test
    public void testRestore2() throws IOException {
	SimpleRetrieveBlock store = new SimpleRetrieveBlock();
	FileSystem fs = new FakeFileSystem();
	RestoreManager r = new RestoreManager(fs, store);

	r.addFile("test.dat", TestData.KEY_CONTENT.getFullKey());
	r.addFile("test2.dat", TestData.KEY_CONTENT.getFullKey());

	r.step();

	assertEquals(0, r.getProgress());
	assertEquals(Arrays.asList(TestData.KEY_CONTENT.getFullKey()), r
		.getPending());
	assertEquals(false, r.isDone());
    }
}
