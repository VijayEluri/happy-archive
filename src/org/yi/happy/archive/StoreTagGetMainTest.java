package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
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
     * fetch {@link TestData#TAG_FILES}.
     * 
     * @throws IOException
     */
    @Test
    public void testSimple() throws IOException {
        MapClearBlockSource source = new MapClearBlockSource();
        source.put(C1);
        source.put(C2);

        FragmentSaveMemory target = new FragmentSaveMemory();

        InputStream in = input(IN);

        new StoreTagGetMain(source, target, null, in).run();

        assertArrayEquals(raw(D1), target.get(N1));
        assertArrayEquals(raw(D2), target.get(N2));
    }

    /**
     * fetch {@link TestData#TAG_FILES}. With not everything ready at the same
     * time.
     * 
     * @throws IOException
     */
    @Test
    public void testNotReady() throws IOException {
        final MapClearBlockSource source = new MapClearBlockSource();
        FragmentSaveMemory target = new FragmentSaveMemory();

        NotReadyHandler notReady = new NotReadyHandler() {
            private int call = 0;

            @Override
            public void onNotReady(RestoreEngine engine, boolean progress)
                    throws IOException {
                call++;
                if (call == 1) {
                    assertFalse(progress);
                    source.put(C1);
                    return;
                }
                if (call == 2) {
                    assertTrue(progress);
                    return;
                }
                if (call == 3) {
                    assertFalse(progress);
                    source.put(C2);
                    return;
                }
                fail();
            }
        };

        InputStream in = input(IN);
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
}
