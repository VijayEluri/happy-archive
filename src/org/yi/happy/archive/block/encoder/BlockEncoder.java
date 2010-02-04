package org.yi.happy.archive.block.encoder;

import org.yi.happy.archive.block.Block;

/**
 * an encoder for blocks.
 */
public interface BlockEncoder {
    /**
     * Encode a block.
     * 
     * @param block
     *            the block to encode.
     * @return the resulting block and key.
     */
    public BlockEncoderResult encode(Block block);
}
