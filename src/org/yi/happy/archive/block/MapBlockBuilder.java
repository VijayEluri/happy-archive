package org.yi.happy.archive.block;

import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.key.FullKey;

/**
 * A builder to help create a {@link MapBlock}.
 */
public class MapBlockBuilder {

    private List<MapBlock.Entry> entries;
    private int size;

    /**
     * Create a builder o help create a {@link MapBlock}.
     */
    public MapBlockBuilder() {
        entries = new ArrayList<MapBlock.Entry>();
        size = 0;
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
     * @param entry
     */
    public void add(MapBlock.Entry entry) {
        entries.add(entry);
        size += entry.getEntrySize();
    }

    /**
     * @param key
     * @param offset
     */
    public void add(FullKey key, long offset) {
        add(new MapBlock.Entry(key, offset));
    }

    /**
     * @return the total size of the entries in bytes.
     */
    public int getSize() {
        return size;
    }

    /**
     * @return the number of entries.
     */
    public int count() {
        return entries.size();
    }
}
