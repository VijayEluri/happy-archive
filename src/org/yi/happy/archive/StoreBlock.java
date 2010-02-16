package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

/**
 * A service which encodes and stores blocks.
 */
public interface StoreBlock {

    /**
     * Encode and store a block.
     * 
     * @param block
     *            the block to encode and store.
     * @return the full key for fetching the block in the future.
     * @throws IOException
     *             on storage error.
     */
    FullKey storeBlock(Block block) throws IOException;

}
