package org.yi.happy.archive.block.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.ListBlock;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * A parser for {@link ListBlock}.
 */
public class ListBlockParse {
    /**
     * parse a {@link ListBlock}.
     * 
     * @param block
     *            the {@link Block} to parse.
     * @return a {@link ListBlock}.
     */
    public static ListBlock parseListBlock(Block block) {
        if (block instanceof ListBlock) {
            return (ListBlock) block;
        }

        Map<String, String> meta = block.getMeta();

        if (!meta.keySet().equals(META)) {
            throw new IllegalArgumentException("meta mismatch");
        }

        String type = block.getMeta().get(ListBlock.TYPE_META);
        if (!type.equals(ListBlock.TYPE)) {
            throw new IllegalArgumentException("wrong key-type");
        }

        String body = ByteString.toString(block.getBody());
        String[] lines = body.split("\n");
        List<FullKey> entries = new ArrayList<FullKey>(lines.length);
        for (String line : lines) {
            FullKey key = FullKeyParse.parseFullKey(line);
            entries.add(key);
        }
        return new ListBlock(entries);
    }

    /**
     * check if a block is a {@link ListBlock}.
     * 
     * @param block
     *            the block to check.
     * @return true if the block is a list block.
     */
    public static boolean isListBlock(Block block) {
        String type = block.getMeta().get(ListBlock.TYPE_META);
        if (type == null) {
            return false;
        }
        return type.equals(ListBlock.TYPE);
    }

    private static final Set<String> META = new SetBuilder<String>(
            ListBlock.TYPE_META, ListBlock.SIZE_META).createImmutable();
}
