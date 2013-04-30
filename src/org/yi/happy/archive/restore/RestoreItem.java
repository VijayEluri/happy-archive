package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

/**
 * Represents a block that is in the process of being restored. The block could
 * have data or could refer to other blocks. The model here is a data block or a
 * table of children (key, offset, data).
 */
public interface RestoreItem {

    /**
     * @return true if this item is a data item.
     */
    public boolean isData();

    /**
     * @return true if this item is not loaded.
     */
    public boolean isTodo();

    /**
     * @return the data block to go with this item, if available.
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
     */
    public FullKey getKey(int index);

    /**
     * get the offset of the specified child.
     * 
     * @param index
     *            the index of the child.
     * @return the offset of the specified child, or -1 if not known.
     */
    public long getOffset(int index);

    /**
     * Get the restore item for the specified child.
     * 
     * @param index
     *            the index of the child.
     * @return the restore item for the given child.
     */
    public RestoreItem get(int index);
}
