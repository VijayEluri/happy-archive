package org.yi.happy.archive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StoreRemoveMain}.
 */
public class StoreRemoveMainTest {
    private static TestData C1 = TestData.KEY_CONTENT;
    private static TestData C2 = TestData.KEY_CONTENT_1;
    private static TestData C3 = TestData.KEY_CONTENT_2;

    private static String N = "flush.lst";

    /**
     * check an expected good case.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FileSystem fs = new FakeFileSystem();
        BlockStore store = new StorageMemory();
        store.put(block(C1));
        store.put(block(C2));
        store.put(block(C3));
        fs.save(N, ByteString.toUtf8(key(C2) + "\n" + key(C3) + "\n"));

        List<String> args = Arrays.asList(N);
        new StoreRemoveMain(store, fs, args).run();

        assertTrue(store.contains(key(C1)));
        assertFalse(store.contains(key(C2)));
        assertFalse(store.contains(key(C3)));
    }

    private static EncodedBlock block(TestData item) throws IOException {
        return item.getEncodedBlock();
    }

    private static LocatorKey key(TestData item) {
        return item.getLocatorKey();
    }
}
