package org.yi.happy.archive;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.key.FullKey;

/**
 * allow for retrieval by full key and the automatic decoding of the resulting
 * blocks.
 * 
 * @author sarah dot a dot happy at gmail dot com
 * 
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
     * the storage object I am attched to.
     */
    private BlockStore storage;

    /**
     * get a key from the store, and decode it.
     * 
     * @param key
     *            the full key to read
     * @return the decoded block
     * @throws BlockNotFoundException
     *             if the block is not in the store.
     * @throws IOException
     *             on decoding failure.
     */
    public Block retrieveBlock(FullKey key) throws BlockNotFoundException,
	    DecodeException {
	EncodedBlock block;
	try {
	    block = storage.get(key.toLocatorKey());
	} catch (FileNotFoundException e) {
	    throw new BlockNotFoundException(e);
	} catch (IOException e) {
	    throw new DecodeException(e);
	}

	try {
	    return block.decode(key);
	} catch (IllegalArgumentException e) {
	    throw new DecodeException(e);
	}
    }

    /**
     * check if a key is in the store
     * 
     * @param key
     *            the full key to check for
     * @return true if it is in the store
     * @throws IOException
     */
    public boolean blockHave(FullKey key) throws IOException {
	return storage.contains(key.toLocatorKey());
    }

}
