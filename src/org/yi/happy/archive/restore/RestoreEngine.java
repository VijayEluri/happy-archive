package org.yi.happy.archive.restore;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.DataBlockParse;
import org.yi.happy.archive.Fragment;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.DataBlock;
import org.yi.happy.archive.block.MapBlock;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * The logic required to join data blocks back together. This takes care of the
 * work item list. The usage model is push-pull, push in blocks that are needed,
 * pull out resulting fragments.
 */
public class RestoreEngine {
    private static class WorkItem {
        public FullKey key;
        public Long offset;

        public WorkItem(FullKey key, Long offset) {
            this.key = key;
            this.offset = offset;
        }
    }

    private List<WorkItem> workItems;
    private Deque<Fragment> out;

    /**
     * Set up the logic to join data blocks back together.
     * 
     * @param key
     *            the key to start from.
     */
    public RestoreEngine(FullKey key) {
        this.workItems = new ArrayList<WorkItem>();
        this.workItems.add(new WorkItem(key, 0l));

        this.out = new ArrayDeque<Fragment>();
    }

    /**
     * @return the list of blocks that can be immediately processed. The first
     *         key in the list is the key that is needed if firstOnly is set to
     *         true.
     */
    public List<FullKey> getNeededNow() {
        LinkedHashSet<FullKey> needed = new LinkedHashSet<FullKey>();
        for (WorkItem item : workItems) {
            if (item.offset == null) {
                continue;
            }

            needed.add(item.key);
        }
        return new ArrayList<FullKey>(needed);
    }

    /**
     * @return the list of blocks that are known to be needed later.
     */
    public List<FullKey> getNeededLater() {
        LinkedHashSet<FullKey> needed = new LinkedHashSet<FullKey>();
        for (WorkItem item : workItems) {
            if (item.offset != null) {
                continue;
            }

            needed.add(item.key);
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
        for (WorkItem item : workItems) {
            if (item.offset == null) {
                later.add(item.key);
            } else {
                needed.add(item.key);
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
        return workItems.get(index).key;
    }

    /**
     * fetch the offset for the given item.
     * 
     * @param index
     *            the index of the item.
     * @return the offset, or null if it is not yet known.
     */
    public Long getOffset(int index) {
        return workItems.get(index).offset;
    }

    /**
     * get the ready state of all the work items in one call.
     * 
     * @return a BitSet with a bit set for each work item that is ready.
     */
    public BitSet isReady() {
        BitSet ready = new BitSet();
        int i = 0;
        for (WorkItem item : workItems) {
            if (item.offset != null) {
                ready.set(i);
            }
            i++;
        }
        return ready;
    }

    /**
     * get the ready state of a work item.
     * 
     * @param index
     *            the index to check.
     * @return true if the item is ready for processing.
     */
    public boolean isReady(int index) {
        return workItems.get(index).offset != null;
    }

    /**
     * process some decoded blocks.
     * 
     * @param blocks
     *            the decoded blocks to process.
     * @return true if progress was made.
     */
    public boolean step(Map<FullKey, Block> blocks) {
        boolean progress = false;

        if (workItems.size() == 0) {
            return progress;
        }

        for (int index = 0; index < workItems.size();) {
            process: {
                WorkItem item = workItems.get(index);
                if (item.offset == null) {
                    break process;
                }

                Block block = blocks.get(item.key);
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
        return workItems.isEmpty();
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
     * @return true if progress was made.
     */
    @MagicLiteral
    public boolean step(int index, Block block) {
        WorkItem item = workItems.get(index);

        if (item.offset == null) {
            return false;
        }

        long base = item.offset;

        String type = block.getMeta().get("type");
        if (type == null) {

            DataBlock b;
            try {
                b = DataBlockParse.parse(block);
            } catch (IllegalArgumentException e) {
                throw e;
            }
            Bytes data = b.getBody();

            replace(index, null, base + data.getSize());
            out.add(new Fragment(base, data));
            return true;
        }

        if (type.equals(MapBlock.TYPE)) {

            String map = ByteString.toString(block.getBody().toByteArray());
            String[] lines = map.split("\n");
            List<WorkItem> add = new ArrayList<WorkItem>(lines.length);
            for (String line : lines) {
                String[] cols = line.split("\t", 2);
                FullKey key = FullKeyParse.parseFullKey(cols[0]);
                long offset = Long.parseLong(cols[1]) + base;
                add.add(new WorkItem(key, offset));
            }

            replace(index, add, base);
            return true;
        }

        if (type.equals("list")) {

            String list = ByteString.toString(block.getBody());
            String[] lines = list.split("\n");
            List<WorkItem> add = new ArrayList<WorkItem>(lines.length);
            for (String line : lines) {
                FullKey key = FullKeyParse.parseFullKey(line);
                add.add(new WorkItem(key, null));
            }

            replace(index, add, base);
            return true;
        }

        if (type.equals("split")) {

            String countString = block.getMeta().get("split-count");
            int count = Integer.parseInt(countString);
            List<WorkItem> add = new ArrayList<WorkItem>(count);
            String keyBase = item.key + "/";
            for (int i = 0; i < count; i++) {
                FullKey key = FullKeyParse.parseFullKey(keyBase + i);
                add.add(new WorkItem(key, null));
            }

            replace(index, add, base);
            return true;
        }

        if (type.equals("indirect")) {

            FullKey key = FullKeyParse.parseFullKey(ByteString.toString(block
                    .getBody()));
            List<WorkItem> add = Collections.singletonList(new WorkItem(key,
                    base));

            replace(index, add, base);
            return true;
        }

        throw new IllegalArgumentException("can not handle block type: " + type);
    }

    /**
     * @return the number of work items in the engine.
     */
    public int getItemCount() {
        return workItems.size();
    }

    private void replace(int index, List<WorkItem> with, long base) {
        workItems.remove(index);
        if (with != null) {
            workItems.addAll(index, with);
        }
        fixOffset(index, base);
    }

    /**
     * If the offset is not set for the given item, set it to base.
     * 
     * @param index
     *            the index into the items list.
     * @param base
     *            the base offset for the item.
     */
    private void fixOffset(int index, long base) {
        if (workItems.size() <= index) {
            return;
        }

        WorkItem item = workItems.get(index);

        if (item.offset != null) {
            return;
        }

        item.offset = base;
    }
}
