package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.restore.RestoreEngine;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StoreFileGetMain}.
 */
public class StoreFileGetMainTest {
    private static final String N = "out";

    /**
     * The simple run. Everything is ready.
     * 
     * @throws IOException
     */
    @Test
    public void testSimple() throws IOException {
        final TestData C = TestData.KEY_CONTENT;
        final TestData F = TestData.FILE_CONTENT;

        final FragmentSaveMemory target = new FragmentSaveMemory();

        final ClearBlockSourceMemory store = new ClearBlockSourceMemory();
        store.put(C);

        List<String> args = strs(key(C), N);
        new StoreFileGetMain(store, target, null, args).run();

        assertArrayEquals(raw(F), target.get(N));
    }

    /**
     * When not everything is ready, the not ready handler is called.
     * 
     * @throws IOException
     */
    @Test
    public void testNotReady() throws IOException {
        final TestData M = TestData.KEY_CONTENT_MAP;
        final TestData C1 = TestData.KEY_CONTENT_1;
        final TestData C2 = TestData.KEY_CONTENT_2;
        final TestData F = TestData.FILE_CONTENT_MAP;

        final FragmentSaveMemory target = new FragmentSaveMemory();

        final ClearBlockSourceMemory source = new ClearBlockSourceMemory();

        NotReadyHandler notReady = new NotReadyHandler() {
            private int call = 0;

            @Override
            public void onNotReady(RestoreEngine engine, boolean progress)
                    throws IOException {
                call++;
                if (call == 1) {
                    assertFalse(progress);
                    source.put(M);
                    return;
                }
                if (call == 2) {
                    assertTrue(progress);
                    source.put(C1);
                    return;
                }
                if (call == 3) {
                    assertTrue(progress);
                    return;
                }
                if (call == 4) {
                    assertFalse(progress);
                    source.put(C2);
                    return;
                }
                fail();
            }
        };

        List<String> args = strs(key(M), N);
        new StoreFileGetMain(source, target, notReady, args).run();

        assertArrayEquals(raw(F), target.get(N));
    }

    private static byte[] raw(TestData item) throws IOException {
        return item.getBytes();
    }

    private static List<String> strs(Object... items) {
        List<String> out = new ArrayList<String>();
        for (Object item : items) {
            out.add(item.toString());
        }
        return out;
    }

    private static FullKey key(TestData item) {
        return item.getFullKey();
    }
}
