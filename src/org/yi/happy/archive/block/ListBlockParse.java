package org.yi.happy.archive.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * A parser for {@link ListBlock}.
 */
public class ListBlockParse {
    /**
     * parse a {@link ListBlock}. This does not check that the block is actually
     * a list block.
     * 
     * @param block
     *            the {@link Block} to parse.
     * @return a {@link ListBlock}.
     */
    public static ListBlock parseListBlock(Block block) {
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
        Map<String, String> meta = block.getMeta();
        if (meta.get(MapBlock.TYPE_META) == null) {
            return false;
        }

        return meta.get(MapBlock.TYPE_META).equals(ListBlock.TYPE);
    }
}
