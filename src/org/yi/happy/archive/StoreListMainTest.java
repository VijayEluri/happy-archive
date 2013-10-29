package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StoreListMain}.
 */
public class StoreListMainTest {
    private static TestData C1 = TestData.KEY_CONTENT;
    private static TestData C2 = TestData.KEY_CONTENT_2;
    private static TestData C3 = TestData.KEY_CONTENT_1;

    /**
     * A good run.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        BlockStore store = new StorageMemory();
        store.put(block(C1));
        store.put(block(C2));
        store.put(block(C3));

        CapturePrintStream out = CapturePrintStream.create();

        new StoreListMain(store, out).run();

        String want = key(C1) + "\n" + key(C2) + "\n" + key(C3) + "\n";
        assertEquals(want, out.toString());
    }

    private static EncodedBlock block(TestData item) throws IOException {
        return item.getEncodedBlock();
    }

    private static LocatorKey key(TestData item) {
        return item.getLocatorKey();
    }
}
