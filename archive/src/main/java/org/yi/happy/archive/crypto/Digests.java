package org.yi.happy.archive.crypto;

import java.security.MessageDigest;

import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.UnknownDigestAlgorithmException;

/**
 * utility methods for dealing with digests.
 */
public class Digests {
    private Digests() {

    }

    /**
     * get the hash of some data using a message digest.
     * 
     * @param data
     *            the data to hash
     * @param digest
     *            the digest to use
     * @return the hashed data
     * @throws UnknownDigestAlgorithmException
     *             if the digest implementation is not available.
     */
    public static byte[] digestData(DigestProvider digest, byte[] data)
            throws UnknownDigestAlgorithmException {
        MessageDigest d = digest.get();
        d.update(data);
        return d.digest();
    }

    /**
     * get the hash of some data using a message digest.
     * 
     * @param data
     *            the data to hash
     * @param digest
     *            the digest to use
     * @return the hashed data
     */
    public static byte[] digestData(DigestProvider digest, Bytes data) {
        return digestData(digest, data.toByteArray());
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
    public static byte[] expandKey(DigestProvider digest, byte[] data, int size) {
        MessageDigest d = digest.get();
        byte[] out = new byte[size];
        int o = 0;
        int n = 1;
        while (o < out.length) {
            for (int j = 0; j < n; j++) {
                d.update((byte) 0);
            }
            d.update(data);
            byte[] bit = d.digest();
            for (int j = 0; o < out.length && j < bit.length; j++) {
                out[o++] = bit[j];
            }
            n++;
        }
        return out;
    }
}
