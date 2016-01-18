package org.yi.happy.archive.block.parser;

import java.util.Map;
import java.util.Set;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.DataBlock;

/**
 * A parser for a basic data block.
 */
public class DataBlockParse {

    private static final Set<String> META = new SetBuilder<String>(
            DataBlock.SIZE_META).createImmutable();

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

        if (!meta.keySet().equals(META)) {
            throw new IllegalArgumentException("not a plain data block");
        }

        return new DataBlock(block.getBody());
    }

    /**
     * @param block
     *            the block to check the type of.
     * @return true if the block is a basic data block.
     */
    public static boolean isDataBlock(Block block) {
        return block.getMeta().keySet().equals(META);
    }

}
