package org.yi.happy.archive;

import java.security.MessageDigest;

import org.yi.happy.archive.key.UnknownAlgorithmException;

public interface DigestProvider {
    /**
     * Provide an instance of the digest.
     * 
     * @return an instance of the digest.
     * @throws UnknownAlgorithmException
     *             if the algorithm does not have a known implementation.
     */
    MessageDigest get() throws UnknownAlgorithmException;

    /**
     * @return the digest algorithm that will be provided.
     */
    String getAlgorithm();
}
