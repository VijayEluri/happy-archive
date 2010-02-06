package org.yi.happy.archive.block.parser;

import org.yi.happy.archive.VerifyException;

public class NegativeSizeException extends VerifyException {

    public NegativeSizeException() {
	super();
    }

    public NegativeSizeException(String message, Throwable cause) {
	super(message, cause);
    }

    public NegativeSizeException(String message) {
	super(message);
    }

    public NegativeSizeException(Throwable cause) {
	super(cause);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -100640332648989878L;

}
