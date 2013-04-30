package org.yi.happy.archive.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

public class ListBlockParse {
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

    public static boolean isListBlock(Block block) {
        Map<String, String> meta = block.getMeta();
        if (meta.get(MapBlock.TYPE_META) == null) {
            return false;
        }

        return meta.get(MapBlock.TYPE_META).equals(ListBlock.TYPE);
    }
}
