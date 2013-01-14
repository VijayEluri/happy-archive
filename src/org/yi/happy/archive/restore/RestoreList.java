package org.yi.happy.archive.restore;

import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

public class RestoreList implements RestoreItem {

    public static class Child {

        private final FullKey key;
        private long offset;
        private RestoreItem item;

        public Child(FullKey key, long offset) {
            this(key, offset, new RestoreTodo());
        }

        public Child(FullKey key, long offset, RestoreItem item) {
            this.key = key;
            this.offset = offset;
            this.item = item;
        }

        public void setOffset(long offset) {
            if (this.offset >= 0) {
                throw new IllegalStateException();
            }
            this.offset = offset;
        }

        public void setItem(RestoreItem item) {
            this.item = item;
        }

        public FullKey getKey() {
            return this.key;
        }

        public long getOffset() {
            return this.offset;
        }

        public RestoreItem getItem() {
            return this.item;
        }
    }

    private Block block;
    private ArrayList<Child> children;

    public RestoreList(Block block, List<RestoreList.Child> children) {
        this.block = block;
        this.children = new ArrayList<Child>(children.size());
        for (Child child : children) {
            this.children.add(new Child(child.getKey(), child.getOffset(),
                    child.getItem()));
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
        return children.size();
    }

    @Override
    public RestoreItem get(int index) {
        return children.get(index).getItem();
    }

    @Override
    public FullKey getKey(int index) {
        return children.get(index).getKey();
    }

    @Override
    public long getOffset(int index) {
        return children.get(index).getOffset();
    }
}
