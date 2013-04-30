package org.yi.happy.archive.block;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

public class IndirectBlockParse {

    public static boolean isIndirectBlock(Block block) {
        String type = block.getMeta().get("type");
        if (type == null) {
            return false;
        }

        return type.equals("indirect");
    }

    public static IndirectBlock parseIndirectBlock(Block block) {
        FullKey key = FullKeyParse.parseFullKey(ByteString.toString(block
                .getBody()));
        return new IndirectBlock(key);
    }

}
