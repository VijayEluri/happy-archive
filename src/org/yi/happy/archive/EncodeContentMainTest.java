package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import org.junit.Test;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.EnvBuilder;
import org.yi.happy.archive.file_system.FakeFileSystem;
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
        FakeFileSystem fs = new FakeFileSystem();
        fs.save("in.dat", TestData.CLEAR_CONTENT.getBytes());
        StringWriter out = new StringWriter();

        EncodeContentMain app = new EncodeContentMain(fs, out);
        Env env = new EnvBuilder().addArgument("in.dat").addArgument("out.dat")
                .create();
        app.run(env);

        assertEquals(
                TestData.KEY_CONTENT_AES128.getFullKey().toString() + "\n", out
                .toString());
        assertArrayEquals(TestData.KEY_CONTENT_AES128.getBytes(), fs
                .load("out.dat"));
    }
}
