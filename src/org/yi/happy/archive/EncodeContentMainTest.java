package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link EncodeContentMain}.
 */
public class EncodeContentMainTest {
    /**
     * an expected good usage.
     * 
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        FileStoreMemory fs = new FileStoreMemory();
        fs.put("in.dat", TestData.CLEAR_CONTENT.getBytes());
        CapturePrintStream out = CapturePrintStream.create();

        List<String> args = Arrays.asList("in.dat", "out.dat");
        new EncodeContentMain(fs, out, args).run();

        assertEquals(
                TestData.KEY_CONTENT_AES128.getFullKey().toString() + "\n", out
                .toString());
        assertArrayEquals(TestData.KEY_CONTENT_AES128.getBytes(), fs
                .get("out.dat"));
    }
}
