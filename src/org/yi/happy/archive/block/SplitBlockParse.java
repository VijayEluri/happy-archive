package org.yi.happy.archive.block;

import org.yi.happy.annotate.MagicLiteral;

public class SplitBlockParse {

    @MagicLiteral
    public static SplitBlock parseSplitBlock(Block block) {
        if (block instanceof SplitBlock) {
            return (SplitBlock) block;
        }

        String countString = block.getMeta().get("split-count");
        int count = Integer.parseInt(countString);

        return new SplitBlock(count);
    }

    @MagicLiteral
    public static boolean isSplitBlock(Block block) {
        String type = block.getMeta().get("type");
        if (type == null) {
            return false;
        }

        return type.equals("split");
    }

}
