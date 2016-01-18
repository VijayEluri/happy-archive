package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.encoder.BlockEncoder;
import org.yi.happy.archive.block.encoder.BlockEncoderResult;
import org.yi.happy.archive.key.FullKey;

/**
 * an implementation of the {@link ClearBlockTarget} service that uses a
 * {@link BlockEncoder} service and a {@link BlockStore} service.
 */
public class ClearBlockTargetStore implements ClearBlockTarget {

    private final BlockStore blocks;
    private final BlockEncoder encoder;

    /**
     * 
     * @param encoder
     * @param blocks
     */
    public ClearBlockTargetStore(BlockEncoder encoder, BlockStore blocks) {
        this.encoder = encoder;
        this.blocks = blocks;
    }

    @Override
    public FullKey put(Block block) throws IOException {
        BlockEncoderResult e = encoder.encode(block);
        blocks.put(e.getBlock());
        return e.getKey();
    }

}
