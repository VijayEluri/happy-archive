package org.yi.happy.archive.block.parser;

import java.util.Map;
import java.util.Set;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.SplitBlock;


/**
 * A parser for {@link SplitBlock}.
 */
public class SplitBlockParse {
    /**
     * make a {@link SplitBlock}.
     * 
     * @param block
     *            the {@link Block} to parse.
     * @return a {@link SplitBlock}.
     */
    public static SplitBlock parseSplitBlock(Block block) {
        if (block instanceof SplitBlock) {
            return (SplitBlock) block;
        }

        Map<String, String> meta = block.getMeta();

        if (!meta.keySet().equals(META)) {
            throw new IllegalArgumentException("meta mismatch");
        }

        String type = block.getMeta().get(SplitBlock.TYPE_META);
        if (!type.equals(SplitBlock.TYPE)) {
            throw new IllegalArgumentException("wrong key-type");
        }

        String countString = block.getMeta().get(SplitBlock.SPLIT_COUNT_META);
        int count = Integer.parseInt(countString);

        return new SplitBlock(count);
    }

    /**
     * check if a block is a split block.
     * 
     * @param block
     *            the {@link Block} to check.
     * @return true if the {@link Block} is a split block.
     */
    public static boolean isSplitBlock(Block block) {
        String type = block.getMeta().get(SplitBlock.TYPE_META);
        if (type == null) {
            return false;
        }

        return type.equals(SplitBlock.TYPE);
    }

    private static final Set<String> META = new SetBuilder<String>(
            SplitBlock.SPLIT_COUNT_META, SplitBlock.TYPE_META,
            SplitBlock.SIZE_META).createImmutable();
}
