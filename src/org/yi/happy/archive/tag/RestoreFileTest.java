package org.yi.happy.archive.tag;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.SimpleRetrieveBlock;
import org.yi.happy.archive.SplitReader;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link RestoreFile}.
 */
public class RestoreFileTest {
    /**
     * restore a file with all the blocks initially available.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
	FileSystem fs = new FakeFileSystem();
	SimpleRetrieveBlock store = new SimpleRetrieveBlock();
	store.put(TestData.KEY_CONTENT);

	RestoreFile f = new RestoreFile(new SplitReader(TestData.KEY_CONTENT
		.getFullKey(), store), "test.dat", fs);
	f.step();

	assertEquals(true, f.isDone());
	assertArrayEquals(TestData.FILE_CONTENT.getBytes(), fs.load("test.dat"));
    }
}
