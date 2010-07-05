package org.yi.happy.archive;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.key.FullKey;

/**
 * Block retrieval and decoding service that connects to a block store.
 */
public class RetrieveBlockStorage implements RetrieveBlock {

    /**
     * create attached to a storage object
     * 
     * @param storage
     *            the storage object
     */
    public RetrieveBlockStorage(BlockStore storage) {
        this.storage = storage;
    }

    /**
     * the storage service I am attached to.
     */
    private final BlockStore storage;

    @Override
    public Block retrieveBlock(FullKey key) throws IOException {
        EncodedBlock b;
        try {
            b = storage.get(key.toLocatorKey());
            if (b == null) {
                return null;
            }
        } catch (IllegalArgumentException e) {
            throw null;
        } catch (FileNotFoundException e) {
            return null;
        }

        try {
            return b.decode(key);
        } catch (IllegalArgumentException e) {
            throw new DecodeException(e);
        }
    }
}
