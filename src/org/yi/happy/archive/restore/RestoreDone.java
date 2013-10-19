package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

/**
 * A place holder for cleared data blocks in a {@link RestoreItem} tree.
 */
public class RestoreDone implements RestoreItem {

    private final long size;

    /**
     * create with a size.
     * 
     * @param size
     *            the size.
     */
    public RestoreDone(long size) {
        this.size = size;
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
        return null;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public int count() {
        return 0;
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
    public RestoreItem get(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void set(int index, RestoreItem item) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void clear(int index) {
        throw new IndexOutOfBoundsException();
    }
}
