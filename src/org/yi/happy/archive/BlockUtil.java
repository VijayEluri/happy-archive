package org.yi.happy.archive;

import java.security.MessageDigest;

import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.UnknownAlgorithmException;

/**
 * Utility methods for blocks of data
 */
public class BlockUtil {

    /**
     * check that a block is internally consistent
     * 
     * @param block
     *            the block to check
     * @throws VerifyException
     *             if the verify fails
     */
    @Deprecated
    public static void verify(Block block) throws VerifyException {
	EncodedBlockFactory.parse(block);
    }

    /**
     * decode a block given a full key
     * 
     * @param block
     *            the block to decode
     * @param fullKey
     *            the full key
     * @return a decoded block
     * @throws IllegalArgumentException
     *             if the key type is not recognized
     * @deprecated use the decode method of an encoded block instead.
     */
    @Deprecated
    public static Block decode(Block block, FullKey fullKey) {
	return EncodedBlockFactory.parse(block).decode(fullKey);
    }

    /**
     * expand some data into an encryption key using the same algorithm as
     * freenet does for key expansion. the first size bytes of ( digest('\0' +
     * data) + digest("\0\0" + data) + ... ).
     * 
     * @param digest
     *            the digest to use for the expansion
     * @param data
     *            the seed data
     * @param size
     *            the target key size
     * @return the key bytes
     */
    public static byte[] expandKey(MessageDigest digest, byte[] data, int size) {
	byte[] out = new byte[size];
	int o = 0;
	int n = 1;
	while (o < out.length) {
	    for (int j = 0; j < n; j++) {
		digest.update((byte) 0);
	    }
	    digest.update(data);
	    byte[] bit = digest.digest();
	    for (int j = 0; o < out.length && j < bit.length; j++) {
		out[o++] = bit[j];
	    }
	    n++;
	}
	return out;
    }

    public static byte[] hashBytes(String digest, byte[] bytes)
	    throws UnknownDigestException {
	try {
	    MessageDigest d = DigestFactory.create(digest);
	    d.update(bytes);
	    return d.digest();
	} catch (UnknownAlgorithmException e) {
	    throw new UnknownDigestException(digest, e);
	}
    }
}
