package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

public class RestoreData implements RestoreItem {

    private final Block block;

    public RestoreData(Block block) {
        this.block = block;
    }

    @Override
    public boolean isData() {
        return true;
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
        return 0;
    }

    @Override
    public RestoreItem get(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void set(int index, RestoreItem item) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public FullKey getKey(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public long getOffset(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void setOffset(int index, long offset) {
        throw new IndexOutOfBoundsException();
    }
}
