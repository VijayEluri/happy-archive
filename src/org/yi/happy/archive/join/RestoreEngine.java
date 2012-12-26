package org.yi.happy.archive.join;

import java.util.ArrayList;
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

public class RestoreEngine {
    private static class Pending {
        public FullKey key;
        public Long offset;

        public Pending(FullKey key, Long offset) {
            this.key = key;
            this.offset = offset;
        }
    }

    private List<Pending> todo;
    private FragmentHandler handler;

    public RestoreEngine(FullKey key, FragmentHandler handler) {
        todo = new ArrayList<Pending>();
        todo.add(new Pending(key, 0l));

        this.handler = handler;
    }

    public List<FullKey> getNeededNow() {
        LinkedHashSet<FullKey> needed = new LinkedHashSet<FullKey>();
        for (Pending item : todo) {
            if (item.offset == null) {
                continue;
            }

            needed.add(item.key);
        }
        return new ArrayList<FullKey>(needed);
    }

    @MagicLiteral
    public void addBlocks(Map<FullKey, Block> blocks) {
        if (todo.size() == 0) {
            return;
        }

        for (int index = 0; index < todo.size();) {
            Pending item = todo.get(index);

            if (item.offset == null) {
                index++;
                continue;
            }

            Block block = blocks.get(item.key);
            if (block == null) {
                index++;
                continue;
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
                continue;
            }

            if (type.equals(MapBlock.TYPE)) {
                String map = ByteString.toString(block.getBody().toByteArray());
                String[] lines = map.split("\n");
                List<Pending> add = new ArrayList<Pending>(lines.length);
                for (String line : lines) {
                    String[] cols = line.split("\t", 2);
                    FullKey key = FullKeyParse.parseFullKey(cols[0]);
                    long offset = Long.parseLong(cols[1]) + base;
                    add.add(new Pending(key, offset));
                }

                replace(index, add, base);
                continue;
            }

            if (type.equals("list")) {
                String list = ByteString.toString(block.getBody());
                String[] lines = list.split("\n");
                List<Pending> add = new ArrayList<Pending>(lines.length);
                for (String line : lines) {
                    FullKey key = FullKeyParse.parseFullKey(line);
                    add.add(new Pending(key, null));
                }

                replace(index, add, base);
                continue;
            }

            throw new IllegalArgumentException("can not handle block type: "
                    + type);
        }

        if (todo.size() == 0) {
            handler.end();
        }
    }

    private void replace(int index, List<Pending> with, long base) {
        todo.remove(index);
        if (with != null) {
            todo.addAll(index, with);
        }
        fixOffset(index, base);
    }

    /**
     * If the offset is not set for the given item, set it to base.
     * 
     * @param index
     *            the index into the todo list of the item.
     * @param base
     *            the base offset for the item.
     */
    private void fixOffset(int index, long base) {
        if (todo.size() <= index) {
            return;
        }

        Pending item = todo.get(index);

        if (item.offset != null) {
            return;
        }

        item.offset = base;
    }

    public List<FullKey> getNeededLater() {
        LinkedHashSet<FullKey> needed = new LinkedHashSet<FullKey>();
        for (Pending item : todo) {
            if (item.offset != null) {
                continue;
            }

            needed.add(item.key);
        }
        return new ArrayList<FullKey>(needed);
    }
}
