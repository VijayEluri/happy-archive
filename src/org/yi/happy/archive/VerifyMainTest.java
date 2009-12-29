package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import org.junit.Test;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.test_data.TestData;

public class VerifyMainTest {
	@Test
	public void testOk() throws Exception {
		StringWriter out = new StringWriter();
		FakeFileSystem fs = new FakeFileSystem();
		fs.save(TestData.KEY_CONTENT.getFileName(), TestData.KEY_CONTENT
				.getBytes());
		VerifyMain app = new VerifyMain(fs, out);

		app.run(TestData.KEY_CONTENT.getFileName());
		
		assertEquals("ok " + TestData.KEY_CONTENT.getLocatorKey() + " "
				+ TestData.KEY_CONTENT.getFileName() + "\n", out.toString());
	}

	@Test
	public void testMissing() throws Exception {
		StringWriter out = new StringWriter();
		FakeFileSystem fs = new FakeFileSystem();
		VerifyMain app = new VerifyMain(fs, out);

		app.run("file.dat");

		assertEquals("fail file.dat\n", out.toString());
	}
}
