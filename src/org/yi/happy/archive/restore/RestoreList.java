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
    public FullKey getKey(int index) {
        return block.get(index);
    }

    @Override
    public long getOffset(int index) {
        return offsets[index];
    }
}
