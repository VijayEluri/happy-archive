package org.yi.happy.archive;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.annotate.NeedFailureTest;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.EnvBuilder;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link FileStoreBlockPutMain}.
 */
@NeedFailureTest
public class FileStoreBlockPutMainTest {
    /**
     * A normal good usage test.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FakeFileSystem fs = new FakeFileSystem();
        StorageMemory store = new StorageMemory();
        fs.save("block.dat", TestData.KEY_CONTENT.getBytes());

        Env env = new EnvBuilder().withStore("store").addArgument("block.dat")
                .create();
        FileStoreBlockPutMain main = new FileStoreBlockPutMain(store, fs, env);
        main.run();

        assertTrue(store.contains(TestData.KEY_CONTENT.getLocatorKey()));
    }
}
