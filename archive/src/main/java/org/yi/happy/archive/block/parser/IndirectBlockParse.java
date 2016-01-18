package org.yi.happy.archive.block.parser;

import java.util.Map;
import java.util.Set;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.IndirectBlock;
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
        if (block instanceof IndirectBlock) {
            return (IndirectBlock) block;
        }

        Map<String, String> meta = block.getMeta();

        if (!meta.keySet().equals(META)) {
            throw new IllegalArgumentException("meta mismatch");
        }

        String type = block.getMeta().get(IndirectBlock.TYPE_META);
        if (!type.equals(IndirectBlock.TYPE)) {
            throw new IllegalArgumentException("wrong key-type");
        }

        FullKey key = FullKeyParse.parseFullKey(ByteString.toString(block
                .getBody()));
        return new IndirectBlock(key);
    }

    private static final Set<String> META = new SetBuilder<String>(
            IndirectBlock.TYPE_META, IndirectBlock.SIZE_META).createImmutable();
}
