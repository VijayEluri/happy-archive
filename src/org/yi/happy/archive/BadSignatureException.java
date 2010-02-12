package org.yi.happy.archive;

/**
 * Signal that a verification signature was found to be invalid.
 */
public class BadSignatureException extends IllegalArgumentException {

    /**
     * 
     */
    private static final long serialVersionUID = -5672592544914810341L;

    public BadSignatureException() {
    }

    public BadSignatureException(String message, Throwable cause) {
	super(message, cause);
    }

    public BadSignatureException(String s) {
	super(s);
    }

    public BadSignatureException(Throwable cause) {
	super(cause);
    }

}
