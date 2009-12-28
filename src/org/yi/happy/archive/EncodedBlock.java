package org.yi.happy.archive;

import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.LocatorKey;

/**
 * an encoded and validated block of data.
 * 
 * @author sarah dot a dot happy at gmail dot com
 */
public interface EncodedBlock extends Block {
    /**
     * 
     * @return the locator key for this block.
     */
    public LocatorKey getKey();

    /**
     * 
     * @return the cipher for this block.
     */
    public String getCipher();

    /**
     * 
     * @return the digest for this block.
     */
    public String getDigest();

    /**
     * 
     * @return the size of the body of this block in bytes.
     */
    public int getSize();

    /**
     * @return a copy the body of the block. This array is safe to modify.
     */
    public byte[] getBody();

    public byte[] asBytes();

    /**
     * decode the block using the given full key.
     * 
     * @param fullKey
     *            the full key.
     * @return the decoded block.
     */
    public Block decode(FullKey fullKey);

    /**
     * @return the total size of this block in bytes.
     */
    public int getRawSize();
}
