package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link DecodeBlockMain}.
 */
public class DecodeBlockMainTest {
    /**
     * an expected successful run.
     * 
     * @throws Exception
     */
    @Test
    @SmellsMessy
    public void test1() throws Exception {
        FakeFileSystem fs = new FakeFileSystem();
        fs.save("test.dat", TestData.KEY_BLOB.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        new DecodeBlockMain(fs, out).run("test.dat", TestData.KEY_BLOB
                .getFullKey().toString());

        assertArrayEquals(TestData.CLEAR_CONTENT.getBytes(), out.toByteArray());
    }
}
