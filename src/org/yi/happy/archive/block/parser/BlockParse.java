package org.yi.happy.archive.block.parser;

import java.util.Map;

import org.yi.happy.annotate.ExternalValue;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.IndirectBlock;
import org.yi.happy.archive.block.ListBlock;
import org.yi.happy.archive.block.MapBlock;
import org.yi.happy.archive.block.SplitBlock;

/**
 * Parse any type of block into the specific form.
 */
public class BlockParse {
    /**
     * Parse any block.
     * 
     * @param bytes
     *            the block data.
     * @return a {@link Block}.
     */
    public static Block parse(byte[] bytes) {
        Block block = GenericBlockParse.parse(bytes);
        return parse(block);
    }

    /**
     * the meta-data field name for type.
     */
    @ExternalValue
    public static final String TYPE_META = "type";

    /**
     * Parse any type of block into the specific form.
     * 
     * @param block
     *            a {@link Block}.
     * @return a {@link Block}.
     */
    public static Block parse(Block block) {
        Map<String, String> meta = block.getMeta();

        if (meta.containsKey(EncodedBlock.KEY_TYPE_META)) {
            return EncodedBlockParse.parse(block);
        }

        String type = meta.get(TYPE_META);
        if (type == null) {
            return DataBlockParse.parse(block);
        }

        if (type.equals(MapBlock.TYPE)) {
            return MapBlockParse.parseMapBlock(block);
        }

        if (type.equals(ListBlock.TYPE)) {
            return ListBlockParse.parseListBlock(block);
        }

        if (type.equals(SplitBlock.TYPE)) {
            return SplitBlockParse.parseSplitBlock(block);
        }

        if (type.equals(IndirectBlock.TYPE)) {
            return IndirectBlockParse.parseIndirectBlock(block);
        }

        return block;
    }
}
