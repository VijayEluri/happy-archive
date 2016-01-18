package org.yi.happy.archive.block.parser;

import java.util.Map;
import java.util.Set;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.MapBlock;
import org.yi.happy.archive.block.MapBlockBuilder;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * parsing for {@link MapBlock}.
 */
public class MapBlockParse {
    /**
     * parse a {@link MapBlock}.
     * 
     * @param block
     *            the block to parse.
     * @return a {@link MapBlock}.
     */
    public static MapBlock parseMapBlock(Block block) {
        if (block instanceof MapBlock) {
            return (MapBlock) block;
        }

        Map<String, String> meta = block.getMeta();

        if (!meta.keySet().equals(META)) {
            throw new IllegalArgumentException("meta mismatch");
        }

        String type = block.getMeta().get(MapBlock.TYPE_META);
        if (!type.equals(MapBlock.TYPE)) {
            throw new IllegalArgumentException("wrong key-type");
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

    /**
     * check if a block is a {@link MapBlock}.
     * 
     * @param block
     *            the {@link Block} to check.
     * @return true if the block is a {@link MapBlock}.
     */
    public static boolean isMapBlock(Block block) {
        Map<String, String> meta = block.getMeta();
        if (meta.get(MapBlock.TYPE_META) == null) {
            return false;
        }

        return meta.get(MapBlock.TYPE_META).equals(MapBlock.TYPE);
    }

    private static final Set<String> META = new SetBuilder<String>(
            MapBlock.TYPE_META, MapBlock.SIZE_META).createImmutable();
}
