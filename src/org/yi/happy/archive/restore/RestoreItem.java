package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

public interface RestoreItem {

    public boolean isData();

    public boolean isTodo();

    public Block getBlock();

    /**
     * @return the number of children
     */
    public int count();

    public FullKey getKey(int index);

    public long getOffset(int index);

    public RestoreItem get(int index);
}
