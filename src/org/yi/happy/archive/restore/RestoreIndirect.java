package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.IndirectBlock;
import org.yi.happy.archive.key.FullKey;

/**
 * A {@link RestoreItem} for an {@link IndirectBlock}.
 */
public class RestoreIndirect implements RestoreItem {

    private final IndirectBlock block;

    /**
     * make from an {@link IndirectBlock}.
     * 
     * @param block
     *            the {@link IndirectBlock}.
     */
    public RestoreIndirect(IndirectBlock block) {
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
}
