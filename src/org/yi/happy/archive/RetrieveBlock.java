package org.yi.happy.archive;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

public interface RetrieveBlock {
    public Block retrieveBlock(FullKey key) throws DecodeException,
	    BlockNotFoundException;
}
