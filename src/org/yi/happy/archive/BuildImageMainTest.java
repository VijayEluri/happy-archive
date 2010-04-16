package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link BuildImageMain}.
 */
public class BuildImageMainTest {
    /**
     * A sample good run.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FileSystem fs = new FakeFileSystem();
        FileBlockStore store = new FileBlockStore(fs, "store");
        store.put(TestData.KEY_CONTENT.getEncodedBlock());
        StringWriter out = new StringWriter();
        fs.save("outstanding", ByteString.toUtf8(TestData.KEY_CONTENT
                .getLocatorKey().toString()
                + "\n"));
        fs.mkdir("output");
        new BuildImageMain(fs, out).run("store", "outstanding", "output",
                "4700");

        assertArrayEquals(TestData.KEY_CONTENT.getBytes(), fs
                .load("output/00000000.dat"));
        assertEquals("1\t1\n", out.toString());
    }

    /**
     * Another sample good run.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        FileSystem fs = new FakeFileSystem();
        FileBlockStore store = new FileBlockStore(fs, "store");
        store.put(TestData.KEY_CONTENT.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_1.getEncodedBlock());
        StringWriter out = new StringWriter();
        fs.save("outstanding", ByteString.toUtf8(TestData.KEY_CONTENT
                .getLocatorKey()
                + "\n" + TestData.KEY_CONTENT_1.getLocatorKey() + "\n"));
        fs.mkdir("output");
        new BuildImageMain(fs, out).run("store", "outstanding", "output",
                "4700");

        assertArrayEquals(TestData.KEY_CONTENT.getBytes(), fs
                .load("output/00000000.dat"));
        assertArrayEquals(TestData.KEY_CONTENT_1.getBytes(), fs
                .load("output/00000001.dat"));
        assertEquals("2\t1\n", out.toString());
    }
}
