package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link FileStoreTagGetMain}.
 */
public class FileStoreTagGetMainTest {
    private static final TestData IN = TestData.TAG_FILES;
    private static final TestData D1 = TestData.FILE_CONTENT;
    private static final TestData D2 = TestData.FILE_CONTENT_40;
    private static final TestData C1 = TestData.KEY_CONTENT;
    private static final TestData C2 = TestData.KEY_CONTENT_40;
    private static final String N1 = "hello.txt";
    private static final String N2 = "test.dat";

    /**
     * restore two files, with all the data already available.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        MapClearBlockSource source = new MapClearBlockSource();
        FileSystem fs = new FakeFileSystem();
        FragmentSave target = new FragmentSaveFileSystem(fs);
        InputStream in = input(IN);

        source.put(C1);
        source.put(C2);

        WaitHandler waitHandler = new WaitHandler() {
            @Override
            public void doWait(boolean progress) throws IOException {
                fail();
            }
        };

        new FileStoreTagGetMain(source, target, fs, waitHandler, in, null)
                .run();

        assertArrayEquals(raw(D1), fs.load(N1));
        assertArrayEquals(raw(D2), fs.load(N2));
    }

    /**
     * restore two files, with all the data available after it is asked for.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        InputStream in = input(IN);
        final FileSystem fs = new FakeFileSystem();
        FragmentSave target = new FragmentSaveFileSystem(fs);
        final MapClearBlockSource source = new MapClearBlockSource();
        final NeedCapture needHandler = new NeedCapture();

        WaitHandler waitHandler = new WaitHandler() {
            @Override
            public void doWait(boolean progress) throws IOException {
                state.doWait(progress);
            }

            private WaitHandler state = new WaitHandler() {
                @Override
                public void doWait(boolean progress) throws IOException {
                    assertFalse(progress);

                    List<LocatorKey> want = keyList(C1, C2);
                    assertEquals(want, needHandler.getKeys());

                    source.put(C1);
                    source.put(C2);

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

        new FileStoreTagGetMain(source, target, fs, waitHandler, in,
                needHandler).run();

        assertArrayEquals(raw(D1), fs.load(N1));
        assertArrayEquals(raw(D2), fs.load(N2));
    }

    private ByteArrayInputStream input(TestData item) throws IOException {
        return new ByteArrayInputStream(item.getBytes());
    }

    private byte[] raw(TestData item) throws IOException {
        return item.getBytes();
    }

    private LocatorKey key(TestData item) {
        return item.getLocatorKey();
    }

    private List<LocatorKey> keyList(TestData... items) {
        List<LocatorKey> out = new ArrayList<LocatorKey>();
        for (TestData item : items) {
            out.add(key(item));
        }
        return out;
    }
}
