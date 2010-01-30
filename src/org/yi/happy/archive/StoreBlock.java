package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.key.FullKey;

public interface StoreBlock {

    FullKey storeBlock(Block block) throws IOException;

}
