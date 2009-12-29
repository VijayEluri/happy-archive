package org.yi.happy.archive;


/**
 * an encoder for blocks.
 * 
 * @author sarah dot a dot happy at gmail dot com
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
