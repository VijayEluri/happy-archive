package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import org.junit.Test;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.test_data.TestData;

public class EncodeContentMainTest {
    @Test
    public void test1() throws Exception {
	FakeFileSystem fs = new FakeFileSystem();
	fs.save("in.dat", TestData.CLEAR_CONTENT.getBytes());
	StringWriter out = new StringWriter();

	EncodeContentMain app = new EncodeContentMain(fs, out);
	app.run("in.dat", "out.dat");

	assertEquals(TestData.KEY_CONTENT.getFullKey().toString() + "\n", out
		.toString());
	assertArrayEquals(TestData.KEY_CONTENT.getBytes(), fs.load("out.dat"));
    }
}
