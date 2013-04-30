package org.yi.happy.archive.restore;

import java.util.Arrays;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.MapBlock;
import org.yi.happy.archive.key.FullKey;

public class RestoreMap implements RestoreItem {

    private final MapBlock block;
    private RestoreItem[] children;

    public RestoreMap(MapBlock block) {
        this.block = block;

        this.children = new RestoreItem[block.count()];
        Arrays.fill(this.children, new RestoreTodo());
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

    @Override
    public RestoreItem get(int index) {
        return children[index];
    }

}
