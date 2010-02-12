package org.yi.happy.archive;

import org.yi.happy.archive.crypto.UnknownAlgorithmException;

public class UnknownDigestAlgorithmException extends UnknownAlgorithmException {

    public UnknownDigestAlgorithmException(String algorithm, Throwable cause) {
	super(algorithm, cause);
    }

    public UnknownDigestAlgorithmException(String algorithm) {
	super(algorithm);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 3863198592781664483L;

}
