package org.yi.happy.archive;

import org.yi.happy.archive.key.LocatorKey;

/**
 * A visitor for keys in a store.
 */
public interface BlockStoreVisitor {
    /**
     * Called for each key that is found in the store, sorted by hash and type.
     * 
     * @param key
     *            the current key.
     */
    public void accept(LocatorKey key);
}
