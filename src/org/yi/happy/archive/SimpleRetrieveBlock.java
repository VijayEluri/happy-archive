package org.yi.happy.archive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * A trivial block retrieval method for testing.
 */
public class SimpleRetrieveBlock implements RetrieveBlock {

    private Map<LocatorKey, EncodedBlock> map = new HashMap<LocatorKey, EncodedBlock>();

    @Override
    public Block retrieveBlock(FullKey key) throws IOException {
        EncodedBlock block = map.get(key.toLocatorKey());
        if (block == null) {
            return null;
        }
        try {
            return block.decode(key);
        } catch (IllegalArgumentException e) {
            throw new DecodeException(e);
        }
    }

    /**
     * make some test data available.
     * 
     * @param data
     *            the test data to make available.
     * @throws IOException
     */
    public void put(TestData data) throws IOException {
        map.put(data.getLocatorKey(), data.getEncodedBlock());
    }
}