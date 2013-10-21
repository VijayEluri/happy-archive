package org.yi.happy.archive.restore;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.Fragment;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

/**
 * The logic required to join data blocks back together. This takes care of the
 * work item list. The usage model is push-pull, push in blocks that are needed,
 * pull out resulting fragments.
 */
public class RestoreEngine {
    private RestoreWork work;
    private Deque<Fragment> out;

    /**
     * Set up the logic to join data blocks back together.
     * 
     * @param key
     *            the key to start from.
     */
    public RestoreEngine(FullKey key) {
        this.work = new RestoreWork(key);
        this.out = new ArrayDeque<Fragment>();
    }

    /**
     * Set up the logic to join data blocks back together.
     * 
     * @param work
     *            the work list to work on.
     */
    public RestoreEngine(RestoreWork work) {
        this.work = work;
        this.out = new ArrayDeque<Fragment>();
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
     * @param index
     *            the index of the item.
     * @return the key needed by the item at the index in the list.
     */
    public FullKey getKey(int index) {
        return work.getKey(index);
    }

    /**
     * fetch the offset for the given item.
     * 
     * @param index
     *            the index of the item.
     * @return the offset, or null if it is not yet known.
     */
    public Long getOffset(int index) {
        return work.getOffset(index);
    }

    /**
     * get the ready state of a work item.
     * 
     * @param index
     *            the index to check.
     * @return true if the item is ready for processing.
     */
    public boolean isReady(int index) {
        return work.getOffset(index) != -1;
    }

    /**
     * process some decoded blocks.
     * 
     * @param blocks
     *            the decoded blocks to process.
     * @return true if progress was made.
     */
    public boolean step(Map<FullKey, Block> blocks) {
        // TODO replace this method with some type of iterator.
        boolean progress = false;

        if (work.count() == 0) {
            return progress;
        }

        for (int index = 0; index < work.count();) {
            process: {
                if (work.getOffset(index) == -1) {
                    break process;
                }

                Block block = blocks.get(work.getKey(index));
                if (block == null) {
                    break process;
                }

                if (step(index, block) == false) {
                    break process;
                }

                /*
                 * the work item has been removed from the list.
                 */

                progress = true;
                continue;
            }

            index++;
        }

        return progress;
    }

    /**
     * @return true if this engine has no more work to do.
     */
    public boolean isDone() {
        return work.count() == 0;
    }

    /**
     * get an output fragment.
     * 
     * @return an output fragment.
     * @throws NoSuchElementException
     *             if there are no output fragments ready.
     */
    public Fragment getOutput() throws NoSuchElementException {
        return out.remove();
    }

    /**
     * check if there is output ready.
     * 
     * @return true if there is output ready.
     */
    public boolean isOutputReady() {
        return out.isEmpty() == false;
    }

    /**
     * do the processing of a work item with the given block.
     * 
     * @param index
     *            the index of the work item.
     * @param block
     *            the decoded block the work item needs.
     * @return true if progress was made (and the item is no longer in the work
     *         list).
     */
    @MagicLiteral
    public boolean step(int index, Block block) {
        long offset = work.getOffset(index);

        if (offset == -1) {
            return false;
        }

        RestoreItem item = RestoreItemFactory.create(work.getKey(index), block);
        work.replace(index, item);

        if (item.isData()) {
            Bytes data = item.getBlock().getBody();
            out.add(new Fragment(offset, data));
        }

        return true;
    }

    /**
     * @return the number of work items in the engine.
     */
    public int getItemCount() {
        return work.count();
    }
}
