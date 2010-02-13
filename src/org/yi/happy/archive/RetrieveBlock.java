package org.yi.happy.archive;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

/**
 * A block retrieval and decoding strategy.
 */
public interface RetrieveBlock {
    /**
     * fetch and decode a block.
     * 
     * @param key
     *            the full key of the block to fetch and decode.
     * @return the decoded block.
     * @throws FileNotFoundException
     *             if the block is not available.
     * @throws IOException
     *             if the block is available but fetching or decoding failed.
     */
    public Block retrieveBlock(FullKey key) throws IOException;
}
