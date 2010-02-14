package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.key.LocatorKey;

public interface BlockStore {
    /**
     * put a block into the store.
     * 
     * @param block
     *            the block to put into the store.
     * @throws IOException
     *             if an error occurred putting the block in the store.
     */
    void put(EncodedBlock block) throws IOException;

    /**
     * get a block from the store.
     * 
     * @param key
     *            the key to fetch.
     * @return the block from the store, or null if the block is not available.
     * @throws IOException
     *             on errors other than the block not being available.
     */
    EncodedBlock get(LocatorKey key) throws IOException;
}
