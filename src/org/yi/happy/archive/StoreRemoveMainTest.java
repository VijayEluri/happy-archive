package org.yi.happy.archive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StoreRemoveMain}.
 */
public class StoreRemoveMainTest {
    private static TestData C1 = TestData.KEY_CONTENT;
    private static TestData C2 = TestData.KEY_CONTENT_1;
    private static TestData C3 = TestData.KEY_CONTENT_2;

    /**
     * check an expected good case.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        BlockStore blocks = new BlockStoreMemory();
        blocks.put(block(C1));
        blocks.put(block(C2));
        blocks.put(block(C3));

        InputStream in = input(key(C2) + "\n" + key(C3) + "\n");

        new StoreRemoveMain(blocks, in).run();

        assertTrue(blocks.contains(key(C1)));
        assertFalse(blocks.contains(key(C2)));
        assertFalse(blocks.contains(key(C3)));
    }

    private static EncodedBlock block(TestData item) throws IOException {
        return item.getEncodedBlock();
    }

    private static LocatorKey key(TestData item) {
        return item.getLocatorKey();
    }

    private static InputStream input(String text) {
        byte[] bytes = ByteString.toUtf8(text);
        return new ByteArrayInputStream(bytes);
    }
}
