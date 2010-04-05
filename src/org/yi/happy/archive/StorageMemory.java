package org.yi.happy.archive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

    @Override
    public <T extends Throwable> void visit(BlockStoreVisitor<T> visitor)
	    throws T {
	final List<LocatorKey> keys = new ArrayList<LocatorKey>(data.keySet());
	Collections.sort(keys, new Comparator<LocatorKey>() {
	    @Override
	    public int compare(LocatorKey o1, LocatorKey o2) {
		int o = Base16.encode(o1.getHash()).compareTo(
			Base16.encode(o2.getHash()));
		if (o != 0) {
		    return o;
		}

		return o1.getType().compareTo(o2.getType());
	    }
	});

	for (LocatorKey key : keys) {
	    visitor.accept(key);
	}
    }
}
