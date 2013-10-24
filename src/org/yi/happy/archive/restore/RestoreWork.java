package org.yi.happy.archive.restore;

import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

/**
 * A {@link RestoreItem} that can be changed by adding entries and replacing
 * entries with the entries of other {@link RestoreItem}s. This is also the work
 * list for {@link RestoreEngine}.
 */
public class RestoreWork implements RestoreItem {
    private static class Entry {
        public final FullKey key;
        public long offset;

        public Entry(FullKey key, long offset) {
            this.key = key;
            this.offset = offset;
        }
    }

    private List<Entry> entries = new ArrayList<Entry>();

    /**
     * create a blank list.
     */
    public RestoreWork() {
    }

    /**
     * create a single item list.
     * 
     * @param key
     *            the key to add to the list.
     */
    public RestoreWork(FullKey key) {
        entries.add(new Entry(key, 0l));
    }

    @Override
    public int count() {
        return entries.size();
    }

    @Override
    public FullKey getKey(int index) {
        return entries.get(index).key;
    }

    @Override
    public long getOffset(int index) {
        return entries.get(index).offset;
    }

    /**
     * add an entry to the table of children.
     * 
     * @param key
     *            the key of the child.
     * @param offset
     *            the offset of the child.
     */
    public void add(FullKey key, long offset) {
        if (entries.isEmpty() && offset == -1) {
            throw new IllegalArgumentException();
        }

        entries.add(new Entry(key, offset));
    }

    @Override
    public boolean isData() {
        return false;
    }

    @Override
    public Block getBlock() {
        return null;
    }

    /**
     * Replace the entry at index with item. The offsets of the added entries
     * are adjusted relative to the entry being replaced. if there is still an
     * entry at index it will have a valid offset.
     * 
     * @param index
     *            the entry to replace.
     * @param item
     *            the entries to replace with.
     * @throws IllegalStateException
     *             if the offset of the entry at index is not known.
     */
    public void replace(int index, RestoreItem item) {
        long base = entries.get(index).offset;

        /*
         * can only replace rows where the offset is known.
         */
        if (base == -1) {
            throw new IllegalStateException();
        }

        List<Entry> add = new ArrayList<Entry>();

        for (int i = 0; i < item.count(); i++) {
            long offset = item.getOffset(i);
            if (offset != -1) {
                offset = base + offset;
            }

            add.add(new Entry(item.getKey(i), offset));
        }

        entries.remove(index);
        entries.addAll(index, add);

        /*
         * fix the offset if the replacement was empty.
         */
        if (index < entries.size() && entries.get(index).offset == -1) {
            if (item.isData()) {
                base = base + item.getBlock().getBody().getSize();
            }
            entries.get(index).offset = base;
        }
    }
}
