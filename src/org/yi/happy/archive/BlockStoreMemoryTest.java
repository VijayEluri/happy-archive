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
 * Tests for {@link BlockStoreMemory}.
 */
public class BlockStoreMemoryTest {
    /**
     * Check out iteration over the keys.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        BlockStore blocks = new BlockStoreMemory();
        blocks.put(TestData.KEY_CONTENT.getEncodedBlock());
        blocks.put(TestData.KEY_CONTENT_1.getEncodedBlock());
        blocks.put(TestData.KEY_CONTENT_2.getEncodedBlock());

        final List<LocatorKey> keys = new ArrayList<LocatorKey>();
        for (LocatorKey key : blocks) {
            keys.add(key);
        }

        assertEquals(Arrays.asList(TestData.KEY_CONTENT.getLocatorKey(),
                TestData.KEY_CONTENT_2.getLocatorKey(), TestData.KEY_CONTENT_1
                        .getLocatorKey()), keys);
    }
}
