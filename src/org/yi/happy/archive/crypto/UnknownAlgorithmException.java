package org.yi.happy.archive.crypto;

/**
 * thrown if an algorithm name is unknown
 */
public class UnknownAlgorithmException extends IllegalArgumentException {
    /**
     * 
     */
    private static final long serialVersionUID = 2065565560705022438L;

    /**
     * build with an algorithm and cause
     * 
     * @param algorithm
     *            the algorithm
     * @param cause
     *            the cause
     */
    public UnknownAlgorithmException(String algorithm, Throwable cause) {
	super(algorithm, cause);
    }

    /**
     * build with just an algorithm name
     * 
     * @param algorithm
     *            the algorithm
     */
    public UnknownAlgorithmException(String algorithm) {
	super(algorithm);
    }

}
