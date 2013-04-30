package org.yi.happy.archive.block;

import java.util.Map;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

public class MapBlockParse {
    public static MapBlock parseMapBlock(Block block) {
        if (block instanceof MapBlock) {
            return (MapBlock) block;
        }

        MapBlockBuilder builder = new MapBlockBuilder();
        String map = ByteString.toString(block.getBody().toByteArray());
        String[] lines = map.split("\n");
        for (String line : lines) {
            String[] cols = line.split("\t", 2);
            FullKey k = FullKeyParse.parseFullKey(cols[0]);
            long offset = Long.parseLong(cols[1]);
            builder.add(k, offset);
        }

        return builder.create();
    }

    public static boolean isMapBlock(Block block) {
        Map<String, String> meta = block.getMeta();
        if (meta.get(MapBlock.TYPE_META) == null) {
            return false;
        }

        return meta.get(MapBlock.TYPE_META).equals(MapBlock.TYPE);
    }
}
