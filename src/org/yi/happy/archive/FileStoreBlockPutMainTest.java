package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link FileStoreBlockPutMain}.
 */
public class FileStoreBlockPutMainTest {
    /**
     * A normal good usage test.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
	FakeFileSystem fs = new FakeFileSystem();
	fs.save("block.dat", TestData.KEY_CONTENT.getBytes());
	FileStoreBlockPutMain main = new FileStoreBlockPutMain(fs);

	main.run("store", "block.dat");

	assertArrayEquals(TestData.KEY_CONTENT.getBytes(), fs
		.load("store/8/87/87c/87c5f6fe4ea801c8eb227b8b218a0659"
			+ "c18ece76b7c200c645ab4364becf68d5-content-hash"));
    }
}
