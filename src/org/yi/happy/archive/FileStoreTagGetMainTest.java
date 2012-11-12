package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.EnvBuilder;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link FileStoreTagGetMain}.
 */
public class FileStoreTagGetMainTest {
    /**
     * restore two files, with all the data already available.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(TestData.TAG_FILES
                .getBytes());
        FileSystem fs = new FakeFileSystem();
        BlockStore store = new StorageMemory();
        store.put(TestData.KEY_CONTENT.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_40.getEncodedBlock());

        WaitHandler waitHandler = new WaitHandler() {
            @Override
            public void doWait(boolean progress) throws IOException {
                fail();
            }
        };

        Env env = new EnvBuilder().withStore("store").withNeed("request")
                .create();
        new FileStoreTagGetMain(store, fs, waitHandler, in, null).run(env);

        assertArrayEquals(TestData.FILE_CONTENT.getBytes(), fs
                .load("hello.txt"));
        assertArrayEquals(TestData.FILE_CONTENT_40.getBytes(), fs
                .load("test.dat"));
    }

    /**
     * restore two files, with all the data available after it is asked for.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(TestData.TAG_FILES
                .getBytes());
        final FileSystem fs = new FakeFileSystem();
        final BlockStore store = new StorageMemory();

        WaitHandler waitHandler = new WaitHandler() {
            @Override
            public void doWait(boolean progress) throws IOException {
                state.doWait(progress);
            }

            private WaitHandler state = new WaitHandler() {
                @Override
                public void doWait(boolean progress) throws IOException {
                    assertFalse(progress);

                    String want = TestData.KEY_CONTENT.getLocatorKey() + "\n"
                            + TestData.KEY_CONTENT_40.getLocatorKey() + "\n";
                    assertArrayEquals(ByteString.toUtf8(want), fs
                            .load("request"));

                    store.put(TestData.KEY_CONTENT.getEncodedBlock());
                    store.put(TestData.KEY_CONTENT_40.getEncodedBlock());

                    state = state1;
                }
            };

            private WaitHandler state1 = new WaitHandler() {
                @Override
                public void doWait(boolean progress) throws IOException {
                    fail();
                }
            };
        };

        Env env = new EnvBuilder().withStore("store").withNeed("request")
                .create();
        new FileStoreTagGetMain(store, fs, waitHandler, in, null).run(env);

        assertArrayEquals(TestData.FILE_CONTENT.getBytes(), fs
                .load("hello.txt"));
        assertArrayEquals(TestData.FILE_CONTENT_40.getBytes(), fs
                .load("test.dat"));
    }
}
