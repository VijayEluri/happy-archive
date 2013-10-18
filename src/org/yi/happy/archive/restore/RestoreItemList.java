package org.yi.happy.archive.restore;

import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

/**
 * A restore item that is a list of items that is populated using
 * {@link #add(FullKey, long, RestoreItem)}.
 */
public class RestoreItemList implements RestoreItem {
    private List<FullKey> keys = new ArrayList<FullKey>();
    private List<Long> offsets = new ArrayList<Long>();
    private List<RestoreItem> items = new ArrayList<RestoreItem>();

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

    @Override
    public void setOffset(int index, long offset) {
        if (offsets.get(index) != -1) {
            throw new IllegalStateException();
        }
        offsets.set(index, offset);
    }

    @Override
    public RestoreItem get(int index) {
        return items.get(index);
    }

    @Override
    public void set(int index, RestoreItem item) {
        if (items.get(index).isTodo() != true) {
            throw new IllegalStateException();
        }
        items.set(index, item);
    }

    /**
     * add an entry to the table of children.
     * 
     * @param key
     *            the key of the child.
     * @param offset
     *            the offset of the child.
     * @param item
     *            the restore item for the child.
     */
    public void add(FullKey key, long offset, RestoreItem item) {
        if (item == null) {
            item = new RestoreTodo();
        }

        keys.add(key);
        offsets.add(offset);
        items.add(item);
    }

    /**
     * Fill in any missing offsets that can be found.
     */
    public void fillOffset() {
        fillOffset(this);
    }

    private void fillOffset(RestoreItem item) {
        for (int i = 0; i < item.count(); i++) {
            fillOffset(item.get(i));

            if (i < 1) {
                continue;
            }

            if (item.getOffset(i) != -1) {
                continue;
            }

            long offset = item.getOffset(i - 1);
            if (offset == -1) {
                continue;
            }

            long size = item.get(i - 1).getSize();
            if (size == -1) {
                continue;
            }

            item.setOffset(i, offset + size);
        }
    }

    /**
     * Get the tree of restore items that are left after removing all the
     * completed items and flattening nodes as possible.
     * 
     * @return the tree of items left to work on.
     */
    public RestoreItemList getTodo() {
        fillOffset();

        RestoreItemList out = new RestoreItemList();
        getTodo(out, 0, this);
        return out;
    }

    private void getTodo(RestoreItemList out, long offset, RestoreItem item) {
        for(int i = 0; i < item.count(); i++) {
            long o = item.getOffset(i);
            RestoreItem d = item.get(i);

            if (o == -1) {
                out.add(item.getKey(i), o, d);
                continue;
            }

            if (d.isTodo()) {
                if (o != -1) {
                    o += offset;
                }
                out.add(item.getKey(i), o, d);
                continue;
            }

            getTodo(out, offset + o, d);
        }
    }

    @Override
    public boolean isData() {
        return false;
    }

    @Override
    public boolean isTodo() {
        return false;
    }

    @Override
    public Block getBlock() {
        return null;
    }

    @Override
    public long getSize() {
        if (count() == 0) {
            return 0;
        }

        long offset = getOffset(count() - 1);
        if (offset == -1) {
            return -1;
        }

        long size = get(count() - 1).getSize();
        if (size == -1) {
            return -1;
        }

        return offset + size;
    }
}
