package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.SplitBlock;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * A {@link RestoreItem} for a {@link SplitBlock}.
 */
public class RestoreSplit implements RestoreItem {
    private final FullKey key;
    private final SplitBlock block;

    /**
     * make from a {@link SplitBlock} and key.
     * 
     * @param key
     *            the key.
     * @param block
     *            the {@link SplitBlock}.
     */
    public RestoreSplit(FullKey key, SplitBlock block) {
        this.key = key;
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
        if (index < 0 || index >= count()) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return 0;
        }
        return -1;
    }
}
