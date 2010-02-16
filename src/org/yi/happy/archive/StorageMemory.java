package org.yi.happy.archive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.yi.happy.archive.block.EncodedBlock;
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

    /**
     * test if the store contains the given key.
     * 
     * @param key
     *            the key to check for.
     * @return true if the store contains the key.
     */
    public boolean contains(LocatorKey key) {
	return data.containsKey(key);
    }

    @Override
    public EncodedBlock get(LocatorKey key) throws IOException {
	return data.get(key);
    }

}
