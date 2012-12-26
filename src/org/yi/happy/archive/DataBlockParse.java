package org.yi.happy.archive;

import java.util.Map;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.DataBlock;

/**
 * parser for a basic data block.
 */
public class DataBlockParse {

    /**
     * parse a basic data block.
     * 
     * @param block
     *            the block to parse.
     * @return the parsed block.
     * @throws IllegalArgumentException
     *             if the block can not be parsed.
     */
    public static DataBlock parse(Block block) {
        if (block instanceof DataBlock) {
            return (DataBlock) block;
        }

        Map<String, String> meta = block.getMeta();

        if (!meta.keySet().equals(Sets.asSet("size"))) {
            throw new IllegalArgumentException("not a plain data block");
        }

        return new DataBlock(block.getBody());
    }

}
