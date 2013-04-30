package org.yi.happy.archive.restore;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.DataBlock;
import org.yi.happy.archive.block.DataBlockParse;
import org.yi.happy.archive.block.IndirectBlock;
import org.yi.happy.archive.block.IndirectBlockParse;
import org.yi.happy.archive.block.ListBlock;
import org.yi.happy.archive.block.ListBlockParse;
import org.yi.happy.archive.block.MapBlock;
import org.yi.happy.archive.block.MapBlockParse;
import org.yi.happy.archive.block.SplitBlock;
import org.yi.happy.archive.block.SplitBlockParse;
import org.yi.happy.archive.key.FullKey;

/**
 * A factory that takes a {@link Block} and makes a {@link RestoreItem}.
 */
public class RestoreItemFactory {

    /**
     * Create the specific restore item based on the block.
     * 
     * @param key
     *            the key of the block (only used for split blocks).
     * @param block
     *            the block.
     * @return the created restore item.
     */
    public static RestoreItem create(FullKey key, Block block) {
        if (DataBlockParse.isDataBlock(block)) {
            DataBlock dataBlock = DataBlockParse.parse(block);
            return new RestoreData(dataBlock);
        }

        if (MapBlockParse.isMapBlock(block)) {
            MapBlock mapBlock = MapBlockParse.parseMapBlock(block);
            return new RestoreMap(mapBlock);
        }

        if (ListBlockParse.isListBlock(block)) {
            ListBlock listBlock = ListBlockParse.parseListBlock(block);
            return new RestoreList(listBlock);
        }

        if (SplitBlockParse.isSplitBlock(block)) {
            SplitBlock splitBlock = SplitBlockParse.parseSplitBlock(block);
            return new RestoreSplit(key, splitBlock);
        }

        if (IndirectBlockParse.isIndirectBlock(block)) {
            IndirectBlock indirectBlock = IndirectBlockParse
                    .parseIndirectBlock(block);
            return new RestoreIndirect(indirectBlock);
        }

        throw new IllegalArgumentException();
    }

}
