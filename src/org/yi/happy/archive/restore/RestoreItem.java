package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

/**
 * Represents a block that is in the process of being restored. The block could
 * be unknown, could be data, or could refer to other blocks. The model here is
 * flags for being unknown or data, a block and a table of children (key,
 * offset, data).
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
     * @return the data block to go with this item, if it is from a data block.
     */
    public Block getBlock();

    /**
     * @return the full size of this restore item, or -1 if not known.
     */
    public long getSize();

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

    /**
     * set the offset of the specified child.
     * 
     * @param index
     *            the index of the child.
     * @param offset
     *            the new offset.
     * @throws IndexOutOfBoundsException
     */
    public void setOffset(int index, long offset);

    /**
     * Get the restore item for the specified child.
     * 
     * @param index
     *            the index of the child.
     * @return the restore item for the given child.
     * @throws IndexOutOfBoundsException
     */
    public RestoreItem get(int index);

    /**
     * set the restore item for the specified child. The item can only be set if
     * the current one has the todo flag set.
     * 
     * @param index
     *            the index of the child.
     * @param item
     *            the restore item to use.
     * @throws IndexOutOfBoundsException
     * @throws IllegalStateException
     */
    public void set(int index, RestoreItem item);

    /**
     * clear the restore item for the specified child. The item can only be
     * cleared if the current one has the data flag set.
     * 
     * @param index
     *            the index of the child.
     * @throws IndexOutOfBoundsException
     */
    public void clear(int index);
}
