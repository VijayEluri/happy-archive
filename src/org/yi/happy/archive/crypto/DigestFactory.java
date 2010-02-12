package org.yi.happy.archive.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.yi.happy.archive.UnknownDigestAlgorithmException;

/**
 * simple factory to create message digest objects
 */
public class DigestFactory {
    /**
     * Get a digest provider for the given algorithm.
     * 
     * @param algorithm
     *            the type of digest to create a provider for
     * @return the digest
     * @throws UnknownAlgorithmException
     *             if the algorithm is unknown
     */
    public static DigestProvider getProvider(String algorithm) {
	return new DigestProvider(algorithm) {
	    @Override
	    public MessageDigest get() throws UnknownAlgorithmException {
		try {
		    return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
		    throw new UnknownDigestAlgorithmException(algorithm, e);
		}
	    }
	};
    }
}
