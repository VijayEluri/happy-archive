package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.encoder.BlockEncoder;
import org.yi.happy.archive.block.encoder.BlockEncoderResult;
import org.yi.happy.archive.key.FullKey;

/**
 * an implementation of the {@link StoreBlock} service that uses a
 * {@link BlockEncoder} service and a {@link BlockStore} service.
 */
public class StoreBlockStorage implements StoreBlock {

    private final BlockStore store;
    private final BlockEncoder encoder;

    /**
     * 
     * @param encoder
     * @param store
     */
    public StoreBlockStorage(BlockEncoder encoder, BlockStore store) {
        this.encoder = encoder;
        this.store = store;
    }

    @Override
    public FullKey storeBlock(Block block) throws IOException {
        BlockEncoderResult e = encoder.encode(block);
        store.put(e.getBlock());
        return e.getKey();
    }

}
