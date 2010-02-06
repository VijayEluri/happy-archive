package org.yi.happy.archive;

import java.security.MessageDigest;

/**
 * Utility methods for blocks of data
 */
public class BlockUtil {
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
}
