package org.yi.happy.archive.restore;

import java.util.Arrays;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.SplitBlock;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

public class RestoreSplit implements RestoreItem {

    private final FullKey key;
    private RestoreItem[] children;
    private final SplitBlock block;
    private long[] offsets;

    public RestoreSplit(FullKey key, SplitBlock block) {
        this.key = key;
        this.block = block;

        int count = block.getCount();

        this.children = new RestoreItem[count];
        Arrays.fill(children, new RestoreTodo());

        this.offsets = new long[count];
        Arrays.fill(offsets, -1);
        if (count > 0) {
            offsets[0] = 0;
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
        return block;
    }

    @Override
    public int count() {
        return block.getCount();
    }

    @Override
    public FullKey getKey(int index) {
        if (index < 0 || index >= count()) {
            throw new IndexOutOfBoundsException();
        }
        return FullKeyParse.parseFullKey(key + "/" + index);
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
