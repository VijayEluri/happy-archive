package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

public class RestoreIndirect implements RestoreItem {

    private final Block block;
    private final FullKey key;
    private RestoreItem child;

    public RestoreIndirect(Block block, FullKey key) {
        this.block = block;
        this.key = key;
        this.child = new RestoreTodo();
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
        return 1;
    }

    @Override
    public FullKey getKey(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return key;
    }

    @Override
    public long getOffset(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return 0;
    }

    @Override
    public RestoreItem get(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return child;
    }

}
