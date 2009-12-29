package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

public class DecodeBlockMainTest {
	@Test
	public void test1() throws Exception {
		FakeFileSystem fs = new FakeFileSystem();
		fs.save("test.dat", TestData.KEY_BLOB.getBytes());
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		new DecodeBlockMain(fs, out).run("test.dat", TestData.KEY_BLOB
				.getFullKey().toString());

		assertArrayEquals(TestData.CLEAR_CONTENT.getBytes(), out.toByteArray());
	}
}
