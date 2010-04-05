package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link FileBlockStore}.
 */
public class FileBlockStoreTest {
    /**
     * Check out the key iterator.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FileSystem fs = new FakeFileSystem();
        String base = "store";
        BlockStore store = new FileBlockStore(fs, base);
        store.put(TestData.KEY_CONTENT.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_1.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_2.getEncodedBlock());

        final List<LocatorKey> keys = new ArrayList<LocatorKey>();
        BlockStoreVisitor<RuntimeException> visitor = new BlockStoreVisitor<RuntimeException>() {
            public void accept(LocatorKey key) {
                keys.add(key);
            }
        };
        store.visit(visitor);

        assertEquals(Arrays.asList(TestData.KEY_CONTENT.getLocatorKey(),
                TestData.KEY_CONTENT_2.getLocatorKey(), TestData.KEY_CONTENT_1
                        .getLocatorKey()), keys);
    }
}
