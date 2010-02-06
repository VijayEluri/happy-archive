package org.yi.happy.archive.block;

import java.util.Map;

import org.yi.happy.archive.DigestProvider;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.LocatorKey;

/**
 * an encoded and validated block of data.
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
    public DigestProvider getDigest();

    /**
     * @return all the headers for the block.
     */
    @Override
    public Map<String, String> getMeta();

    /**
     * @return a copy the body of the block. This array is safe to modify.
     */
    @Override
    public byte[] getBody();

    /**
     * @return the whole block encoded as bytes.
     */
    @Override
    public byte[] asBytes();

    /**
     * decode the block using the given full key.
     * 
     * @param fullKey
     *            the full key.
     * @return the decoded block.
     */
    public Block decode(FullKey fullKey);
}
