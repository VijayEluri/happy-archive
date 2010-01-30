package org.yi.happy.archive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.yi.happy.archive.key.LocatorKey;

/**
 * A storage driver backed by memory. So far this could probably be replaced
 * with a logging StoreBlock implementation.
 */
public class StorageMemory implements BlockStore {
    private Map<LocatorKey, EncodedBlock> data = new HashMap<LocatorKey, EncodedBlock>();

    @Override
    public void put(EncodedBlock block) throws IOException {
	data.put(block.getKey(), block);
    }

    public boolean blockHave(LocatorKey key) {
	return data.containsKey(key);
    }

}
