package org.yi.happy.archive.restore;

import org.yi.happy.archive.key.FullKey;

/**
 * A cursor over the leaves of a RestoreItem tree.
 */
public class RestoreCursor {
    private static class Path {
        public final Path path;
        public final RestoreItem item;
        public final int index;

        public Path(Path path, RestoreItem item, int index) {
            this.path = path;
            this.item = item;
            this.index = index;
        }
    }

    private Path path;
    private RestoreItem item;

    /**
     * set up the cursor by giving the root of the tree.
     * 
     * @param root
     *            the root of the tree.
     */
    public RestoreCursor(RestoreItem root) {
        path = null;
        item = root;
    }

    /**
     * @return true if the cursor is at a leaf.
     */
    public boolean isLeaf() {
        return item.count() == 0;
    }

    /**
     * move to the next leaf. If any offsets can be filled in along the way, it
     * does that too.
     * 
     * @return true if cursor has moved to the next leaf.
     */
    public boolean nextLeaf() {
        Path path = this.path;
        RestoreItem item = this.item;
        
        if (isLeaf()) {
            /*
             * find the first edge up the path that has a next edge
             */
            while(true) {
                if (path == null) {
                    return false;
                }
                if (path.index + 1 < path.item.count()) {
                    break;
                }
                path = path.path;
            }
            /*
             * follow the next edge
             */
            path = new Path(path.path, path.item, path.index + 1);
            item = path.item.get(path.index);

            fixOffset(path.item, path.index);
        }

        /*
         * follow down to the first leaf
         */
        while (0 < item.count()) {
            path = new Path(path, item, 0);
            item = item.get(0);
        }

        this.path = path;
        this.item = item;

        return true;
    }

    private void fixOffset(RestoreItem item, int index) {
        if (item.getOffset(index) != -1) {
            return;
        }

        long offset = item.getOffset(index - 1);
        if (offset == -1) {
            return;
        }

        long size = item.get(index - 1).getSize();
        if (size == -1) {
            return;
        }

        item.setOffset(index, offset + size);
    }

    /**
     * 
     * @return true if the current item is todo.
     */
    public boolean isTodo() {
        return item.isTodo();
    }

    /**
     * @return the key of the current item.
     * @throws IllegalStateException
     *             if the cursor is at the root.
     */
    public FullKey getKey() {
        if (path == null) {
            throw new IllegalStateException();
        }
        return path.item.getKey(path.index);
    }

    /**
     * @return the offset of the current item, or -1 if not known.
     */
    public long getOffset() {
        long offset = 0;
        Path path = this.path;
        while (path != null) {
            long base = path.item.getOffset(path.index);
            if (base == -1) {
                return -1;
            }
            offset += base;
            path = path.path;
        }
        return offset;
    }

    /**
     * change the current leaf in the tree.
     * 
     * @param item
     *            the new item.
     * @throws IllegalStateException
     *             if the cursor is at the root.
     */
    public void set(RestoreItem item) {
        if (path == null) {
            throw new IllegalStateException();
        }
        path.item.set(path.index, item);
        this.item = item;
    }

    /**
     * clear the current data leaf.
     */
    public void clear() {
        if (path == null) {
            throw new IllegalStateException();
        }
        path.item.clear(path.index);
        item = path.item.get(path.index);
    }

    /**
     * @return true if the current item is data.
     */
    public boolean isData() {
        return item.isData();
    }

}
