package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.restore.RestoreEngine;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StoreStreamGetMain}.
 */
public class StoreStreamGetMainTest {
    private static final TestData MAP = TestData.KEY_CONTENT_MAP;
    private static final TestData C1 = TestData.KEY_CONTENT_1;
    private static final TestData C2 = TestData.KEY_CONTENT_2;
    private static final Bytes D = new Bytes("0123456789");

    /**
     * an expected good run.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        /*
         * NOTE this is not strictly speaking a unit test since there are two
         * layers of objects in use to exercise the functionality.
         */

        final MapClearBlockSource source = new MapClearBlockSource();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

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

                    source.put(MAP);

                    state = state2;
                }
            };

            private NotReadyHandler state2 = new NotReadyHandler() {
                @Override
                public void onNotReady(RestoreEngine engine, boolean progress)
                        throws IOException {
                    assertTrue(progress);

                    assertEquals(keyList(C1, C2), engine.getNeeded());

                    source.put(C2);

                    state = state3;
                }
            };

            private NotReadyHandler state3 = new NotReadyHandler() {
                @Override
                public void onNotReady(RestoreEngine engine, boolean progress)
                        throws IOException {
                    assertFalse(progress);

                    assertEquals(keyList(C1, C2), engine.getNeeded());

                    source.put(C1);

                    state = state4;
                }
            };

            private NotReadyHandler state4 = new NotReadyHandler() {
                @Override
                public void onNotReady(RestoreEngine engine, boolean progress)
                        throws IOException {
                    fail();
                }
            };
        };

        List<String> args = Arrays.asList(key(MAP).toString());
        new StoreStreamGetMain(source, out, notReady, args).run();

        assertArrayEquals(D.toByteArray(), out.toByteArray());
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
