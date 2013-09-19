package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

public class RestoreTodo implements RestoreItem {

    @Override
    public boolean isData() {
        return false;
    }

    @Override
    public boolean isTodo() {
        return true;
    }

    @Override
    public Block getBlock() {
        return null;
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

    @Override
    public long getSize() {
        return -1;
    }
}
