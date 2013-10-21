package org.yi.happy.archive.restore;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.Fragment;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

/**
 * Assistance with the logic required to use {@link RestoreWork} to put data
 * blocks back together. This insulates the client code from having to deal with
 * {@link RestoreItem}s.
 * 
 * <pre>
 * {@link RestoreEngine} r = new {@link RestoreEngine}(key);
 * 
 * r.start();
 * while(r.findReady()) {
 *   {@link Block} in = getBlockForKey(r.getKey());
 *   if(block == null) {
 *     r.skip();
 *     continue;
 *   }
 *   
 *   {@link Fragment} out = r.step(in);
 *   
 *   if(out != null) {
 *     handleOutput(fragment);
 *   }
 * }
 * </pre>
 */
public class RestoreEngine {
    private RestoreWork work;

    /**
     * Set up the logic to join data blocks back together.
     * 
     * @param key
     *            the key to start from.
     */
    public RestoreEngine(FullKey key) {
        this.work = new RestoreWork(key);
    }

    /**
     * Set up the logic to join data blocks back together.
     * 
     * @param work
     *            the work list to work on.
     */
    public RestoreEngine(RestoreWork work) {
        this.work = work;
    }

    /**
     * @return the list of blocks that can be immediately processed.
     */
    public List<FullKey> getNeededNow() {
        LinkedHashSet<FullKey> needed = new LinkedHashSet<FullKey>();
        for (int index = 0; index < work.count(); index++) {
            if (work.getOffset(index) == -1) {
                continue;
            }

            needed.add(work.getKey(index));
        }
        return new ArrayList<FullKey>(needed);
    }

    /**
     * @return the list of blocks that are known to be needed but can not be
     *         immediately processed.
     */
    public List<FullKey> getNeededLater() {
        LinkedHashSet<FullKey> needed = new LinkedHashSet<FullKey>();
        for (int index = 0; index < work.count(); index++) {
            if (work.getOffset(index) != -1) {
                continue;
            }

            needed.add(work.getKey(index));
        }
        return new ArrayList<FullKey>(needed);
    }

    /**
     * @return the list of blocks that are known to be needed, the blocks that
     *         can be immediately processed are at the beginning of the list.
     */
    public List<FullKey> getNeeded() {
        LinkedHashSet<FullKey> needed = new LinkedHashSet<FullKey>();
        LinkedHashSet<FullKey> later = new LinkedHashSet<FullKey>();
        for (int index = 0; index < work.count(); index++) {
            if (work.getOffset(index) == -1) {
                later.add(work.getKey(index));
            } else {
                needed.add(work.getKey(index));
            }
        }
        needed.addAll(later);
        return new ArrayList<FullKey>(needed);
    }

    /**
     * @return true if this engine has no more work to do.
     */
    public boolean isDone() {
        return work.count() == 0;
    }

    private int index = 0;

    /**
     * Set the cursor back to the beginning.
     */
    public void start() {
        index = 0;
    }

    /**
     * Find a ready item starting with the current item. The first item is
     * always ready when it exists.
     * 
     * @return true if a ready item is found
     */
    public boolean findReady() {
        while (index < work.count()) {
            if (work.getOffset(index) == -1) {
                index++;
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * get the offset of the current item.
     * 
     * @return the offset of the current item.
     */
    public long getOffset() {
        return work.getOffset(index);
    }

    /**
     * get is the key of the current item. This is the key for the block to give
     * to {@link #step(Block)}.
     * 
     * @return the key of the current item.
     */
    public FullKey getKey() {
        return work.getKey(index);
    }

    /**
     * process the current item.
     * 
     * @param block
     *            the needed block.
     * @return the data fragment for data blocks, null otherwise.
     */
    public Fragment step(Block block) {
        long offset = work.getOffset(index);

        if (offset == -1) {
            throw new IllegalStateException();
        }

        RestoreItem item = RestoreItemFactory.create(work.getKey(index), block);
        work.replace(index, item);

        if (item.isData()) {
            Bytes data = item.getBlock().getBody();
            return new Fragment(offset, data);
        }
        return null;
    }

    /**
     * Skip this item.
     */
    public void skip() {
        index++;
    }
}
