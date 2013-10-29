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
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.restore.RestoreEngine;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StoreTagGetMain}.
 */
public class StoreTagGetMainTest {
    private static final TestData IN = TestData.TAG_FILES;
    private static final String N1 = "hello.txt";
    private static final TestData C1 = TestData.KEY_CONTENT;
    private static final TestData D1 = TestData.FILE_CONTENT;
    private static final String N2 = "test.dat";
    private static final TestData C2 = TestData.KEY_CONTENT_40;
    private static final TestData D2 = TestData.FILE_CONTENT_40;

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

        new StoreTagGetMain(source, target, null, in).run();

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

                    assertEquals(keyList(C1, C2), engine.getNeeded());

                    source.put(C1);
                    source.put(C2);

                    state = state1;
                }
            };

            private NotReadyHandler state1 = new NotReadyHandler() {
                @Override
                public void onNotReady(RestoreEngine engine, boolean progress)
                        throws IOException {
                    fail();
                }
            };
        };
        
        new StoreTagGetMain(source, target, notReady, in).run();

        assertArrayEquals(raw(D1), target.get(N1));
        assertArrayEquals(raw(D2), target.get(N2));
    }

    private ByteArrayInputStream input(TestData item) throws IOException {
        return new ByteArrayInputStream(item.getBytes());
    }

    private byte[] raw(TestData item) throws IOException {
        return item.getBytes();
    }

    private FullKey key(TestData item) {
        return item.getFullKey();
    }

    private List<FullKey> keyList(TestData... items) {
        List<FullKey> out = new ArrayList<FullKey>();
        for (TestData item : items) {
            out.add(key(item));
        }
        return out;
    }
}
