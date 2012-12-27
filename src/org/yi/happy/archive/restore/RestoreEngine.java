package org.yi.happy.archive.restore;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

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
 * The logic required to join data blocks back together.
 */
public class RestoreEngine {
    /*
     * convert this to a push-pull data flow instead of a push-push.
     */

    private static class Item {
        public FullKey key;
        public Long offset;
        public Bytes data;

        public Item(FullKey key, Long offset) {
            this.key = key;
            this.offset = offset;
            this.data = null;
        }
    }

    private List<Item> items;
    private FragmentHandler handler;
    private Deque<Fragment> out;

    /**
     * Set up the logic to join data blocks back together.
     * 
     * @param key
     *            the key to start from.
     * @param handler
     *            where to send the fragments as they are encountered.
     */
    public RestoreEngine(FullKey key, FragmentHandler handler) {
        items = new ArrayList<Item>();
        items.add(new Item(key, 0l));

        this.out = new ArrayDeque<Fragment>();

        this.handler = handler;
    }

    public RestoreEngine(FullKey key) {
        this(key, null);
    }

    /**
     * @return the list of blocks that can be immediately processed. The first
     *         key in the list is the key that is needed if firstOnly is set to
     *         true.
     */
    public List<FullKey> getNeededNow() {
        LinkedHashSet<FullKey> needed = new LinkedHashSet<FullKey>();
        for (Item item : items) {
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
        for (Item item : items) {
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
        for (Item item : items) {
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
        return items.get(index).key;
    }

    /**
     * fetch the offset for the given item.
     * 
     * @param index
     *            the index of the item.
     * @return the offset, or null if it is not yet known.
     */
    public Long getOffset(int index) {
        return items.get(index).offset;
    }

    /**
     * 
     * @param index
     * @return true if the item is ready for processing.
     */
    public boolean isReady(int index) {
        return items.get(index).offset != null;
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
        if (items.size() == 0) {
            return progress;
        }

        for (int index = 0; index < items.size();) {
            if (step(blocks, index)) {
                progress = true;
                continue;
            }
            index++;
        }

        if (handler != null && items.size() == 0) {
            handler.end();
        }

        return progress;
    }

    /**
     * @return true if this engine has no more work to do.
     */
    public boolean isDone() {
        return items.isEmpty();
    }

    /**
     * get an output fragment.
     * 
     * @return an output fragment.
     */
    public Fragment getOutput() {
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
     * attempt a processing step on a processing list item. At most one fragment
     * can be emitted to the fragment handler from this call.
     * 
     * @param blocks
     *            the blocks being processed.
     * @param index
     *            the index of the processing item to attempt.
     * @return true if progress was made.
     */
    @MagicLiteral
    public boolean step(Map<FullKey, Block> blocks, int index) {
        /*
         * TODO change to accept a single block.
         */
        Item item = items.get(index);

        if (item.offset == null) {
            return false;
        }

        Block block = blocks.get(item.key);
        if (block == null) {
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

            if (handler != null) {
                handler.data(base, data);
            } else {
                out.add(new Fragment(base, data));
            }
            return true;
        }

        if (type.equals(MapBlock.TYPE)) {
            String map = ByteString.toString(block.getBody().toByteArray());
            String[] lines = map.split("\n");
            List<Item> add = new ArrayList<Item>(lines.length);
            for (String line : lines) {
                String[] cols = line.split("\t", 2);
                FullKey key = FullKeyParse.parseFullKey(cols[0]);
                long offset = Long.parseLong(cols[1]) + base;
                add.add(new Item(key, offset));
            }

            replace(index, add, base);
            return true;
        }

        if (type.equals("list")) {
            String list = ByteString.toString(block.getBody());
            String[] lines = list.split("\n");
            List<Item> add = new ArrayList<Item>(lines.length);
            for (String line : lines) {
                FullKey key = FullKeyParse.parseFullKey(line);
                add.add(new Item(key, null));
            }

            replace(index, add, base);
            return true;
        }

        if (type.equals("split")) {
            String countString = block.getMeta().get("split-count");
            int count = Integer.parseInt(countString);
            List<Item> add = new ArrayList<Item>(count);
            String keyBase = item.key + "/";
            for (int i = 0; i < count; i++) {
                FullKey key = FullKeyParse.parseFullKey(keyBase + i);
                add.add(new Item(key, null));
            }

            replace(index, add, base);
            return true;
        }

        if (type.equals("indirect")) {
            FullKey key = FullKeyParse.parseFullKey(ByteString.toString(block
                    .getBody()));
            List<Item> add = Collections.singletonList(new Item(key, base));

            replace(index, add, base);
            return true;
        }

        throw new IllegalArgumentException("can not handle block type: " + type);
    }

    /**
     * @return the number of work items in the engine.
     */
    public int getItemCount() {
        return items.size();
    }

    private void replace(int index, List<Item> with, long base) {
        items.remove(index);
        if (with != null) {
            items.addAll(index, with);
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
        if (items.size() <= index) {
            return;
        }

        Item item = items.get(index);

        if (item.offset != null) {
            return;
        }

        item.offset = base;
    }
}
