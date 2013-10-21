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
    // TODO make an Entry class to hold the pair of values for each entry.

    private List<FullKey> keys = new ArrayList<FullKey>();
    private List<Long> offsets = new ArrayList<Long>();

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
        keys.add(key);
        offsets.add(0l);
    }

    @Override
    public int count() {
        return keys.size();
    }

    @Override
    public FullKey getKey(int index) {
        return keys.get(index);
    }

    @Override
    public long getOffset(int index) {
        return offsets.get(index);
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
        if (offsets.isEmpty() && offset == -1) {
            throw new IllegalArgumentException();
        }

        keys.add(key);
        offsets.add(offset);
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
        long base = offsets.get(index);

        /*
         * can only replace rows where the offset is known.
         */
        if (base == -1) {
            throw new IllegalStateException();
        }

        List<FullKey> keysAdd = new ArrayList<FullKey>();
        List<Long> offsetsAdd = new ArrayList<Long>();

        for (int i = 0; i < item.count(); i++) {
            long offset = item.getOffset(i);
            if (offset != -1) {
                offset = base + offset;
            }

            keysAdd.add(item.getKey(i));
            offsetsAdd.add(offset);
        }

        keys.remove(index);
        keys.addAll(index, keysAdd);
        offsets.remove(index);
        offsets.addAll(index, offsetsAdd);

        /*
         * fix the offset if the replacement was empty.
         */
        if (index < offsets.size() && offsets.get(index) == -1) {
            if (item.isData()) {
                base = base + item.getBlock().getBody().getSize();
            }
            offsets.set(index, base);
        }
    }
}
