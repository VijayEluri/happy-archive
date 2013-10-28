package org.yi.happy.archive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * A trivial block retrieval method for testing.
 */
public class MapClearBlockSource implements ClearBlockSource {

    private Map<FullKey, Block> map = new HashMap<FullKey, Block>();

    @Override
    public Block get(FullKey key) throws IOException {
        return map.get(key);
    }

    /**
     * make some test data available.
     * 
     * @param data
     *            the test data to make available.
     * @throws IOException
     */
    public void put(TestData data) throws IOException {
        map.put(data.getFullKey(), data.getClearBlock());
    }
}