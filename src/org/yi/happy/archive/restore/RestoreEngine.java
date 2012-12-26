package org.yi.happy.archive.restore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.DataBlockParse;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.DataBlock;
import org.yi.happy.archive.block.MapBlock;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * The logic required to join data blocks back together.
 */
public class RestoreEngine {
    private static class Item {
        public FullKey key;
        public Long offset;

        public Item(FullKey key, Long offset) {
            this.key = key;
            this.offset = offset;
        }
    }

    private List<Item> items;
    private FragmentHandler handler;

    /**
     * True if processing only works from the front of the item list.
     */
    private boolean firstOnly;

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

        firstOnly = false;

        this.handler = handler;
    }

    /**
     * @return the list of blocks that can be immediately processed.
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
     * process some decoded blocks.
     * 
     * @param blocks
     *            the decoded blocks to process.
     */
    public void addBlocks(Map<FullKey, Block> blocks) {
        if (items.size() == 0) {
            return;
        }

        for (int index = 0; index < items.size();) {
            if (step(blocks, index)) {
                continue;
            }
            if (firstOnly) {
                break;
            }
            index++;
        }

        if (items.size() == 0) {
            handler.end();
        }
    }

    /**
     * set if processing only works from the front of the item list.
     * 
     * @param firstOnly
     *            true if processing only works from the front of the item list.
     */
    public void setFirstOnly(boolean firstOnly) {
        this.firstOnly = firstOnly;
    }

    /**
     * @return true if this engine has no more work to do.
     */
    public boolean isDone() {
        return items.isEmpty();
    }

    /**
     * attempt a processing step on a processing list item.
     * 
     * @param blocks
     *            the blocks being processed.
     * @param index
     *            the processing item to attempt.
     * @return true if progress was made.
     */
    @MagicLiteral
    private boolean step(Map<FullKey, Block> blocks, int index) {
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
            handler.data(base, data);
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
