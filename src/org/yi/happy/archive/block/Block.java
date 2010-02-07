package org.yi.happy.archive.block;

import java.util.Map;

import org.yi.happy.archive.Bytes;

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
    public Bytes getBody();

    /**
     * convert the block to bytes.
     * 
     * @return the block as bytes.
     */
    public byte[] asBytes();
}
