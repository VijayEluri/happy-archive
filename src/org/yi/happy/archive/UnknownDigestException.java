package org.yi.happy.archive;

public class UnknownDigestException extends VerifyException {

    public UnknownDigestException() {
	super();
    }

    public UnknownDigestException(String message, Throwable cause) {
	super(message, cause);
    }

    public UnknownDigestException(String message) {
	super(message);
    }

    public UnknownDigestException(Throwable cause) {
	super(cause);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 3863198592781664483L;

}
