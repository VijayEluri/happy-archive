package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.file_system.FileStoreMemory;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link VerifyMain}.
 */
public class VerifyMainTest {
    /**
     * verify a good file.
     * 
     * @throws Exception
     */
    @Test
    public void testOk() throws Exception {
        CapturePrintStream out = CapturePrintStream.create();
        FileStoreMemory fs = new FileStoreMemory();
        fs.put(TestData.KEY_CONTENT.getFileName(), TestData.KEY_CONTENT
                .getBytes());

        List<String> args = Arrays.asList(TestData.KEY_CONTENT.getFileName());
        new VerifyMain(fs, out, args).run();

        assertEquals("ok " + TestData.KEY_CONTENT.getLocatorKey() + " "
                + TestData.KEY_CONTENT.getFileName() + "\n", out.toString());
    }

    /**
     * verify a missing file.
     * 
     * @throws Exception
     */
    @Test
    public void testMissing() throws Exception {
        CapturePrintStream out = CapturePrintStream.create();
        FileStoreMemory fs = new FileStoreMemory();

        List<String> args = Arrays.asList("file.dat");
        VerifyMain app = new VerifyMain(fs, out, args);
        app.run();

        assertEquals("fail file.dat\n", out.toString());
    }
}
