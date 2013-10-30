package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StorageMemory}.
 */
public class StorageMemoryTest {
    /**
     * Check out iteration over the keys.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        BlockStore store = new StorageMemory();
        store.put(TestData.KEY_CONTENT.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_1.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_2.getEncodedBlock());

        final List<LocatorKey> keys = new ArrayList<LocatorKey>();
        for (LocatorKey key : store) {
            keys.add(key);
        }

        assertEquals(Arrays.asList(TestData.KEY_CONTENT.getLocatorKey(),
                TestData.KEY_CONTENT_2.getLocatorKey(), TestData.KEY_CONTENT_1
                        .getLocatorKey()), keys);
    }
}
