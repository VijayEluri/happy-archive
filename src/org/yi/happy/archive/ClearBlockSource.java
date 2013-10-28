package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

/**
 * A block retrieval and decoding service interface.
 */
public interface ClearBlockSource {
    /**
     * Fetch and decode a block. Implementations should return a block if it can
     * be fetched and decoded. In the case where fetching fails in a way that
     * will not prevent fetching of the same key in the future, implementations
     * should return null.
     * 
     * @param key
     *            the full key of the block to fetch and decode.
     * @return the decoded block, or null if the block is not available possibly
     *         because of errors in fetching the block.
     * @throws IOException
     *             if the block is available but fetching or decoding failed,
     *             and it is expected that trying again will continue to fail.
     */
    public Block get(FullKey key) throws IOException;
}
