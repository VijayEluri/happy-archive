package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StoreFileGetMain}.
 */
public class StoreFileGetMainTest {
    private static final Bytes D = new Bytes("0123456789");
    private static final String N = "out";
    private static final TestData MAP = TestData.KEY_CONTENT_MAP;
    private static final TestData C1 = TestData.KEY_CONTENT_1;
    private static final TestData C2 = TestData.KEY_CONTENT_2;

    /**
     * an expected good run.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        final FragmentSaveMemory target = new FragmentSaveMemory();
        final MapClearBlockSource store = new MapClearBlockSource();
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

                    /*
                     * check the request list
                     */
                    assertEquals(keyList(MAP), needHandler.getKeys());

                    /*
                     * add the map block to the store
                     */
                    store.put(MAP);

                    state = state2;
                }
            };

            private final WaitHandler state2 = new WaitHandler() {
                @Override
                public void doWait(boolean progress) throws IOException {
                    assertTrue(progress);

                    /*
                     * check the request list
                     */
                    assertEquals(keyList(C1, C2), needHandler.getKeys());

                    /*
                     * add the second part
                     */
                    store.put(C2);

                    state = state3;
                }
            };

            private final WaitHandler state3 = new WaitHandler() {
                @Override
                public void doWait(boolean progress) throws IOException {
                    assertTrue(progress);

                    /*
                     * check the request list
                     */
                    assertEquals(keyList(C1), needHandler.getKeys());

                    /*
                     * add the first part
                     */
                    store.put(C1);

                    state = state4;
                }
            };

            private final WaitHandler state4 = new WaitHandler() {
                @Override
                public void doWait(boolean progress) {
                    fail();
                }
            };

        };

        List<String> args = list(MAP.getFullKey().toString(), N);
        new StoreFileGetMain(store, target, waitHandler, needHandler, args)
                .run();

        assertArrayEquals(D.toByteArray(), target.get(N));
    }

    private <T> List<T> list(T... items) {
        return Arrays.asList(items);
    }

    private List<LocatorKey> keyList(TestData... items) {
        List<LocatorKey> out = new ArrayList<LocatorKey>();
        for (TestData item : items) {
            out.add(item.getLocatorKey());
        }
        return out;
    }

}
