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
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StoreTagGetMain}.
 */
public class StoreTagGetMainTest {
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
        FragmentSaveMemory target = new FragmentSaveMemory();
        InputStream in = input(IN);

        source.put(C1);
        source.put(C2);

        WaitHandler waitHandler = new WaitHandler() {
            @Override
            public void doWait(boolean progress) throws IOException {
                fail();
            }
        };

        new StoreTagGetMain(source, target, waitHandler, in, null).run();

        assertArrayEquals(raw(D1), target.get(N1));
        assertArrayEquals(raw(D2), target.get(N2));
    }

    /**
     * restore two files, with all the data available after it is asked for.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        InputStream in = input(IN);
        FragmentSaveMemory target = new FragmentSaveMemory();
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

        new StoreTagGetMain(source, target, waitHandler, in, needHandler)
                .run();

        assertArrayEquals(raw(D1), target.get(N1));
        assertArrayEquals(raw(D2), target.get(N2));
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
