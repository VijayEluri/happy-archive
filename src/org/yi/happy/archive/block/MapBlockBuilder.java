package org.yi.happy.archive.block;

import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.key.FullKey;

/**
 * A builder to help create a {@link MapBlock}.
 */
public class MapBlockBuilder {

    private List<MapBlock.Entry> entries;

    /**
     * Create a builder o help create a {@link MapBlock}.
     */
    public MapBlockBuilder() {
        entries = new ArrayList<MapBlock.Entry>();
    }

    /**
     * Create the map block.
     * 
     * @return a new {@link MapBlock} based on the state of this builder.
     */
    public MapBlock create() {
        return new MapBlock(entries);
    }

    /**
     * @param key
     * @param offset
     */
    public void add(FullKey key, long offset) {
        MapBlock.Entry entry = new MapBlock.Entry(key, offset);
        entries.add(entry);
    }
}
