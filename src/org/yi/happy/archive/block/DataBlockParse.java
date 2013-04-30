package org.yi.happy.archive.block;

import java.util.Map;

import org.yi.happy.archive.Sets;

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

    public static boolean isDataBlock(Block block) {
        String type = block.getMeta().get("type");
        return type == null;
    }

}
