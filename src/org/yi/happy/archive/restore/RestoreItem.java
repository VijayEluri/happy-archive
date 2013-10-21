package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

/**
 * Represents a loaded block that can be given to {@link RestoreEngine}. The
 * block could be data, or could refer to other blocks. The model here is a
 * flags for data, a block, and a table of children (key, offset).
 */
public interface RestoreItem {

    /**
     * @return true if this item is a data item.
     */
    public boolean isData();

    /**
     * @return the data block to go with this item, if it is from a data block.
     */
    public Block getBlock();

    /**
     * @return the number of children
     */
    public int count();

    /**
     * Get the key of the specified child.
     * 
     * @param index
     *            the index of the child.
     * @return the key of the block for the specified child.
     * @throws IndexOutOfBoundsException
     */
    public FullKey getKey(int index);

    /**
     * get the offset of the specified child.
     * 
     * @param index
     *            the index of the child.
     * @return the offset of the specified child, or -1 if not known.
     * @throws IndexOutOfBoundsException
     */
    public long getOffset(int index);
}
