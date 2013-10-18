package org.yi.happy.archive.block;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * Parsing functions for {@link IndirectBlock}.
 */
public class IndirectBlockParse {

    /**
     * is this an indirect block?
     * 
     * @param block
     *            the block to check.
     * @return true if the block is an indirect block.
     */
    public static boolean isIndirectBlock(Block block) {
        String type = block.getMeta().get(IndirectBlock.TYPE_META);
        if (type == null) {
            return false;
        }

        return type.equals(IndirectBlock.TYPE);
    }

    /**
     * parse an indirect block.
     * 
     * @param block
     *            the block to parse.
     * @return the parsed block.
     */
    public static IndirectBlock parseIndirectBlock(Block block) {
        FullKey key = FullKeyParse.parseFullKey(ByteString.toString(block
                .getBody()));
        return new IndirectBlock(key);
    }
}
