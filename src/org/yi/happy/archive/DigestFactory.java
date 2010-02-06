package org.yi.happy.archive;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.yi.happy.archive.key.UnknownAlgorithmException;

/**
 * simple factory to create message digest objects
 */
public class DigestFactory {
    /**
     * create a digest for the given algorithm
     * 
     * @param algorithm
     *            the type of digest to create
     * @return the digest
     * @throws UnknownAlgorithmException
     *             if the algorithm is unknown
     */
    @Deprecated
    public static MessageDigest create(String algorithm) {
	try {
	    return MessageDigest.getInstance(algorithm);
	} catch (NoSuchAlgorithmException e) {
	    throw new UnknownAlgorithmException(algorithm, e);
	}
    }

    public static DigestProvider getProvider(String algorithm) {
	return new DigestProvider(algorithm) {
	    @Override
	    public MessageDigest get() throws UnknownAlgorithmException {
		try {
		    return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
		    throw new UnknownAlgorithmException(algorithm, e);
		}
	    }
	};
    }
}
