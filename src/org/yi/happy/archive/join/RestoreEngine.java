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

            long offset = item.offset;

            String type = block.getMeta().get("type");
            if (type == null) {
                DataBlock b;
                try {
                    b = DataBlockParse.parse(block);
                } catch (IllegalArgumentException e) {
                    throw e;
                }
                Bytes data = b.getBody();

                todo.remove(index);
                if (todo.size() > index && todo.get(index).offset == null) {
                    todo.get(index).offset = offset + data.getSize();
                }
                handler.data(offset, data);
                continue;
            }

            if (type.equals(MapBlock.TYPE)) {
                String map = ByteString.toString(block.getBody().toByteArray());
                String[] lines = map.split("\n");
                List<Pending> add = new ArrayList<Pending>(lines.length);
                for (String line : lines) {
                    String[] cols = line.split("\t", 2);
                    FullKey key = FullKeyParse.parseFullKey(cols[0]);
                    long o = Long.parseLong(cols[1]) + offset;
                    add.add(new Pending(key, o));
                }

                todo.remove(index);
                todo.addAll(index, add);
                continue;
            }

            throw new IllegalArgumentException("can not handle block type: "
                    + type);
        }

        if (todo.size() == 0) {
            handler.end();
        }
    }
}
