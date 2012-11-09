package org.yi.happy.archive;

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
 * tests for {@link VolumeGetMain}.
 */
public class VolumeGetMainTest {
    /**
     * load a file from a volume.
     * 
     * @throws UnsupportedOperationException
     * @throws IOException
     */
    @Test
    public void test1() throws UnsupportedOperationException, IOException {
        FileSystem fs = new FakeFileSystem();
        fs.mkdir("/media");
        fs.save("/media/00.dat", TestData.KEY_CONTENT_MAP.getBytes());
        InputStream in = new ByteArrayInputStream("00.dat\n".getBytes("UTF-8"));

        Env env = new EnvBuilder().withStore("store").addArgument("/media")
                .create();
        new VolumeGetMain(fs, in, null, null).run(env);

        FileBlockStore s = new FileBlockStore(fs, "store");
        assertEquals(TestData.KEY_CONTENT_MAP.getEncodedBlock(), s
                .get(TestData.KEY_CONTENT_MAP.getLocatorKey()));
    }
}
