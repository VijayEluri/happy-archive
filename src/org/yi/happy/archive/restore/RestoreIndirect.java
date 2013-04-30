package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.IndirectBlock;
import org.yi.happy.archive.key.FullKey;

public class RestoreIndirect implements RestoreItem {

    private final IndirectBlock block;
    private RestoreItem child;

    public RestoreIndirect(IndirectBlock block) {
        this.block = block;
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
        return block.getKey();
    }

    @Override
    public long getOffset(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return 0;
    }

    @Override
    public void setOffset(int index, long offset) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        throw new IllegalStateException();
    }

    @Override
    public RestoreItem get(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return child;
    }

    @Override
    public void set(int index, RestoreItem item) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }

        if (child.isTodo() == false) {
            throw new IllegalStateException();
        }

        child = item;
    }
}
