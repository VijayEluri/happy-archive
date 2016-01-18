package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.MapBlock;
import org.yi.happy.archive.key.FullKey;

/**
 * The {@link RestoreItem} for a {@link MapBlock}.
 */
public class RestoreMap implements RestoreItem {
    private final MapBlock block;

    /**
     * make from a {@link MapBlock}.
     * 
     * @param block
     *            the {@link MapBlock}.
     */
    public RestoreMap(MapBlock block) {
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
        return block.getKey(index);
    }

    @Override
    public long getOffset(int index) {
        return block.getOffset(index);
    }
}
