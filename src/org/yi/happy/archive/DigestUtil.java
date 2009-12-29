package org.yi.happy.archive;

import java.security.MessageDigest;

/**
 * utility functions for with making digests
 * 
 * @author sarah dot a dot happy at gmail dot com
 */
public class DigestUtil {
    private DigestUtil() {

    }

    /**
     * get the hash of data using digest. resets digest first, then updates it
     * with data, and finally calls digest.
     * 
     * @param data
     *            the data to hash
     * @param digest
     *            the digest to use
     * @return the hashed data
     */
    public static byte[] digestData(byte[] data, MessageDigest digest) {
        digest.reset();
        digest.update(data);
        return digest.digest();
    }

}
