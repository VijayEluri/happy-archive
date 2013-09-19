package org.yi.happy.archive.restore;

import java.util.Arrays;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.ListBlock;
import org.yi.happy.archive.key.FullKey;

public class RestoreList implements RestoreItem {
    private ListBlock block;
    private long[] offsets;
    private RestoreItem[] children;

    public RestoreList(ListBlock block) {
        this.block = block;

        this.offsets = new long[block.count()];
        Arrays.fill(offsets, -1);
        if (offsets.length > 0) {
            offsets[0] = 0;
        }

        this.children = new RestoreItem[block.count()];
        Arrays.fill(children, new RestoreTodo());
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
        return block;
    }

    @Override
    public int count() {
        return block.count();
    }

    @Override
    public RestoreItem get(int index) {
        return children[index];
    }

    @Override
    public void set(int index, RestoreItem item) {
        if (children[index].isTodo() == false) {
            throw new IllegalStateException();
        }

        children[index] = item;
    }

    @Override
    public FullKey getKey(int index) {
        return block.get(index);
    }

    @Override
    public long getOffset(int index) {
        return offsets[index];
    }

    @Override
    public void setOffset(int index, long offset) {
        if (offsets[index] >= 0) {
            throw new IllegalStateException();
        }
        offsets[index] = offset;
    }

    @Override
    public long getSize() {
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
