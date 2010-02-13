package org.yi.happy.archive;

import java.util.Map;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.DataBlock;

public class DataBlockParse {

    public static DataBlock parse(Block block) {
	if (block instanceof DataBlock) {
	    return (DataBlock) block;
	}

	Map<String, String> meta = block.getMeta();

	if (!meta.keySet().equals(Sets.asSet("size"))) {
	    throw new IllegalArgumentException("not a plain data block");
	}

	int size = Integer.parseInt(meta.get("size"));
	if (size != block.getBody().getSize()) {
	    throw new IllegalArgumentException("size mismatch");
	}

	return new DataBlock(block.getBody());
    }

}
