package org.yi.happy.archive;

import org.yi.happy.archive.crypto.UnknownAlgorithmException;

/**
 * signals that a digest algorithm is not known.
 */
public class UnknownDigestAlgorithmException extends UnknownAlgorithmException {

    /**
     * signal that a digest algorithm is not known.
     * 
     * @param algorithm
     *            the algorithm that is not known.
     * @param cause
     *            the cause.
     */
    public UnknownDigestAlgorithmException(String algorithm, Throwable cause) {
	super(algorithm, cause);
    }

    /**
     * signal that a digest algorithm is not known.
     * 
     * @param algorithm
     *            the algorithm that is not known.
     */
    public UnknownDigestAlgorithmException(String algorithm) {
	super(algorithm);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 3863198592781664483L;

}
