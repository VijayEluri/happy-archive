package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.EnvBuilder;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link FileStoreStreamPutMain}.
 */
public class FileStoreStreamPutMainTest {
    /**
     * an expected good store.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FileSystem fs = new FakeFileSystem();
        InputStream in = new ByteArrayInputStream(ByteString.toBytes("hello\n"));
        CapturePrintStream out = CapturePrintStream.create();

        Env env = new EnvBuilder().withStore("store").create();
        new FileStoreStreamPutMain(fs, in, out).run(env);

        assertEquals(TestData.KEY_CONTENT_AES128.getFullKey() + "\n", out
                .toString());

        assertArrayEquals(TestData.KEY_CONTENT_AES128.getBytes(), fs
                .load("store/d/d7/d78/d7859e105484ff5af15fc35365043e92531402b"
                        + "23168246b2cfca4932bf27d14-content-hash"));
    }
}
