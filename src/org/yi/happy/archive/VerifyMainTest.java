package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.EnvBuilder;
import org.yi.happy.archive.file_system.FakeFileSystem;
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
        FakeFileSystem fs = new FakeFileSystem();
        fs.save(TestData.KEY_CONTENT.getFileName(), TestData.KEY_CONTENT
                .getBytes());

        Env env = new EnvBuilder().addArgument(
                TestData.KEY_CONTENT.getFileName()).create();
        new VerifyMain(fs, out, env).run();

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
        FakeFileSystem fs = new FakeFileSystem();

        Env env = new EnvBuilder().addArgument("file.dat").create();
        VerifyMain app = new VerifyMain(fs, out, env);
        app.run();

        assertEquals("fail file.dat\n", out.toString());
    }
}
