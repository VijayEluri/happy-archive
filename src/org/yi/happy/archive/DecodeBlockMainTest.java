package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.file_system.FileStoreMemory;
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
        FileStoreMemory fs = new FileStoreMemory();
        fs.put("test.dat", TestData.KEY_BLOB.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List<String> args = Arrays.asList("test.dat", TestData.KEY_BLOB
                .getFullKey().toString());
        new DecodeBlockMain(fs, out, args).run();

        assertArrayEquals(TestData.CLEAR_CONTENT.getBytes(), out.toByteArray());
    }
}
