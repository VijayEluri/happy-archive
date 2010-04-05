package org.yi.happy.archive;

import org.yi.happy.archive.key.LocatorKey;

/**
 * A visitor for keys in a store.
 * 
 * @param <T>
 *            the exception type
 */
public interface BlockStoreVisitor<T extends Throwable> {
    /**
     * Called for each key that is found in the store, sorted by hash and type.
     * 
     * @param key
     *            the current key.
     * @throws T 
     */
    public void accept(LocatorKey key) throws T;
}
