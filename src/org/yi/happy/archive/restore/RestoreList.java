package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.ListBlock;
import org.yi.happy.archive.key.FullKey;

/**
 * a {@link RestoreItem} for a {@link ListBlock}.
 */
public class RestoreList implements RestoreItem {
    private ListBlock block;

    /**
     * make from a {@link ListBlock}.
     * 
     * @param block
     *            the {@link ListBlock}.
     */
    public RestoreList(ListBlock block) {
        this.block = block;
    }

    @Override
    public boolean isData() {
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
        return block.get(index);
    }

    @Override
    public long getOffset(int index) {
        if (index < 0 || index >= block.count()) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return 0;
        }
        return -1;
    }
}
