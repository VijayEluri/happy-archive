package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

public interface RetrieveBlock {
    public Block retrieveBlock(FullKey key) throws IOException,
	    BlockNotFoundException;
}
