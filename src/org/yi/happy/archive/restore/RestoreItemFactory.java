package org.yi.happy.archive.restore;

import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.MapBlock;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

public class RestoreItemFactory {

    /**
     * Create the specific restore item based on the block.
     * 
     * @param block
     *            the block.
     * @return the created restore item.
     */
    public static RestoreItem create(Block block) {
        String type = block.getMeta().get("type");

        if (type == null) {
            /*
             * TODO parser to turn Block into DataBlock
             */
            return new RestoreData(block);
        }

        if (type.equals(MapBlock.TYPE)) {
            /*
             * TODO parser to turn Block into MapBlock
             */
            String map = ByteString.toString(block.getBody().toByteArray());
            String[] lines = map.split("\n");
            List<RestoreList.Child> children = new ArrayList<RestoreList.Child>(
                    lines.length);
            for (String line : lines) {
                String[] cols = line.split("\t", 2);
                FullKey key = FullKeyParse.parseFullKey(cols[0]);
                long offset = Long.parseLong(cols[1]);
                children.add(new RestoreList.Child(key, offset));
            }
            return new RestoreList(block, children);
        }

        if (type.equals("list")) {
            /*
             * TODO parser to turn Block into ListBlock
             */
            String list = ByteString.toString(block.getBody());
            String[] lines = list.split("\n");
            List<RestoreList.Child> children = new ArrayList<RestoreList.Child>(
                    lines.length);
            for (String line : lines) {
                FullKey key = FullKeyParse.parseFullKey(line);
                children.add(new RestoreList.Child(key, -1));
            }
            if (children.size() > 0) {
                children.get(0).setOffset(0);
            }
            return new RestoreList(block, children);
        }

        if (type.equals("split")) {
            /*
             * TODO parser to turn Block into SplitBlock
             */
            /*
             * XXX need the full key of this block to make this one.
             */
            throw new UnsupportedOperationException();
        }

        if (type.equals("indirect")) {
            FullKey key = FullKeyParse.parseFullKey(ByteString.toString(block
                    .getBody()));
            return new RestoreIndirect(block, key);
        }

        throw new IllegalArgumentException();
    }
}
