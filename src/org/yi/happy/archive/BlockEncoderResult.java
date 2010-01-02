package org.yi.happy.archive;

import org.yi.happy.archive.key.FullKey;

/**
 * the result of doing an encode of a block.
 */
public final class BlockEncoderResult {
    /**
     * 
     * @param key
     *            the full key of the encoded block.
     * @param block
     *            the encoded block.
     */
    public BlockEncoderResult(FullKey key, EncodedBlock block) {
	this.key = key;
	this.block = block;
    }

    private final FullKey key;
    private final EncodedBlock block;

    /**
     * 
     * @return the full key of the encoded block.
     */
    public FullKey getKey() {
	return key;
    }

    /**
     * 
     * @return the encoded block.
     */
    public EncodedBlock getBlock() {
	return block;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((block == null) ? 0 : block.hashCode());
	result = prime * result + ((key == null) ? 0 : key.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	BlockEncoderResult other = (BlockEncoderResult) obj;
	if (block == null) {
	    if (other.block != null)
		return false;
	} else if (!block.equals(other.block))
	    return false;
	if (key == null) {
	    if (other.key != null)
		return false;
	} else if (!key.equals(other.key))
	    return false;
	return true;
    }
}
