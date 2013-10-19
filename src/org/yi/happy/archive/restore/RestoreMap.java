package org.yi.happy.archive.restore;

import java.util.Arrays;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.MapBlock;
import org.yi.happy.archive.key.FullKey;

public class RestoreMap implements RestoreItem {

    private final MapBlock block;
    private RestoreItem[] children;

    public RestoreMap(MapBlock block) {
        this.block = block;

        this.children = new RestoreItem[block.count()];
        Arrays.fill(this.children, new RestoreTodo());
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
    public FullKey getKey(int index) {
        return block.getKey(index);
    }

    @Override
    public long getOffset(int index) {
        return block.getOffset(index);
    }

    @Override
    public void setOffset(int index, long offset) {
        if (index < 0 || index >= count()) {
            throw new IndexOutOfBoundsException();
        }
        throw new IllegalStateException();
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
    public void clear(int index) {
        RestoreItem item = children[index];
        if (item.isData() == false) {
            throw new IllegalStateException();
        }
        item = new RestoreDone(item.getSize());
        children[index] = item;
    }

    @Override
    public long getSize() {
        long size = get(count() - 1).getSize();
        if (size == -1) {
            return -1;
        }
        return getOffset(count() - 1) + size;
    }
}
