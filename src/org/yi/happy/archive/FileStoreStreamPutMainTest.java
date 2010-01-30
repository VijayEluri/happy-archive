package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.junit.Test;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.test_data.TestData;


public class FileStoreStreamPutMainTest {
    @Test
    public void test1() throws IOException {
	FileSystem fs = new FakeFileSystem();
	InputStream in = new ByteArrayInputStream(ByteString.toBytes("hello\n"));
	StringWriter out = new StringWriter();

	new FileStoreStreamPutMain(fs, in, out).run("store");

	assertEquals(TestData.KEY_CONTENT.getFullKey() + "\n", out.toString());

	assertArrayEquals(TestData.KEY_CONTENT.getBytes(), fs
		.load("store/8/87/87c/87c5f6fe4ea801c8eb227b8b218a0659c18ece76"
			+ "b7c200c645ab4364becf68d5-content-hash"));
    }
}
