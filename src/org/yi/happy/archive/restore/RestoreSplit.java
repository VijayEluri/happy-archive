package org.yi.happy.archive.restore;

import java.util.Arrays;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

public class RestoreSplit implements RestoreItem {

    private final FullKey key;
    private final int count;
    private RestoreItem[] children;
    private final Block block;
    private long[] offset;

    public RestoreSplit(FullKey key, int count, Block block) {
        this.key = key;
        this.count = count;
        this.block = block;

        this.children = new RestoreItem[count];
        Arrays.fill(children, new RestoreTodo());
        this.offset = new long[count];
        Arrays.fill(offset, -1);
        if (count > 0) {
            offset[0] = 0;
        }
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
        return count;
    }

    @Override
    public FullKey getKey(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }
        return FullKeyParse.parseFullKey(key + "/" + index);
    }

    @Override
    public long getOffset(int index) {
        return offset[index];
    }

    @Override
    public RestoreItem get(int index) {
        return children[index];
    }

}
