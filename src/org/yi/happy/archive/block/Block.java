package org.yi.happy.archive.block;

import java.util.Map;

import org.yi.happy.annotate.Simplify;

/**
 * a data block is a set of simple headers and a body. The upper size of a block
 * is just over 1 MiB.
 */
public interface Block {
    /**
     * get the meta data (headers).
     * 
     * @return a possibly ordered map of the meta data fields.
     */
    public Map<String, String> getMeta();

    /**
     * get the body of the block.
     * 
     * @return the body of the block.
     */
    public byte[] getBody();

    /**
     * convert the block to bytes.
     * 
     * @return the block as bytes.
     */
    @Simplify("all the implementations do this the same way, delegate to a "
	    + "shared method, or implement it here")
    public byte[] asBytes();
}
