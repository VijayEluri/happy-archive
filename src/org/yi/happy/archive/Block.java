package org.yi.happy.archive;


/**
 * a data block is a set of simple headers and a body. The upper size of a block
 * is just over 1 MiB.
 */
public interface Block {
    /**
     * get a meta data header.
     * 
     * @param name
     *            the meta data header to get.
     * @return the value of the header, or null if it does not exist.
     */
    public String getMeta(String name);

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
    public byte[] asBytes();
}
