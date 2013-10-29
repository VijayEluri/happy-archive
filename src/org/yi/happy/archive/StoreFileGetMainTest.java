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
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.restore.RestoreEngine;
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

        NotReadyHandler notReady = new NotReadyHandler() {
            @Override
            public void onNotReady(RestoreEngine engine, boolean progress)
                    throws IOException {
                state.onNotReady(engine, progress);
            }

            private NotReadyHandler state = new NotReadyHandler() {
                @Override
                public void onNotReady(RestoreEngine engine, boolean progress)
                        throws IOException {
                    assertFalse(progress);

                    assertEquals(keyList(MAP), engine.getNeeded());

                    /*
                     * add the map block to the store
                     */
                    store.put(MAP);

                    state = state2;
                }
            };

            private final NotReadyHandler state2 = new NotReadyHandler() {
                @Override
                public void onNotReady(RestoreEngine engine, boolean progress)
                        throws IOException {
                    assertTrue(progress);

                    assertEquals(keyList(C1, C2), engine.getNeeded());

                    store.put(C2);

                    state = state3;
                }
            };

            private final NotReadyHandler state3 = new NotReadyHandler() {
                @Override
                public void onNotReady(RestoreEngine engine, boolean progress)
                        throws IOException {
                    assertTrue(progress);

                    assertEquals(keyList(C1), engine.getNeeded());

                    store.put(C1);

                    state = state4;
                }
            };

            private final NotReadyHandler state4 = new NotReadyHandler() {
                @Override
                public void onNotReady(RestoreEngine engine, boolean progress)
                        throws IOException {
                    fail();
                }
            };

        };

        List<String> args = list(key(MAP).toString(), N);
        new StoreFileGetMain(store, target, notReady, args).run();

        assertArrayEquals(D.toByteArray(), target.get(N));
    }

    private static <T> List<T> list(T... items) {
        return Arrays.asList(items);
    }

    private static List<FullKey> keyList(TestData... items) {
        List<FullKey> out = new ArrayList<FullKey>();
        for (TestData item : items) {
            out.add(key(item));
        }
        return out;
    }

    private static FullKey key(TestData item) {
        return item.getFullKey();
    }

}
