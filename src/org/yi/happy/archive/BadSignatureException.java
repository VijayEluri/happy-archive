package org.yi.happy.archive;

/**
 * Signal that a verification signature was found to be invalid.
 */
public class BadSignatureException extends IllegalArgumentException {

    /**
     * 
     */
    private static final long serialVersionUID = -5672592544914810341L;

    /**
     * Signal that a verification signature was found to be invalid.
     */
    public BadSignatureException() {
    }

    /**
     * Signal that a verification signature was found to be invalid, with a
     * custom message and a cause.
     * 
     * @param message
     *            the custom message.
     * @param cause
     *            the cause.
     */
    public BadSignatureException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Signal that a verification signature was found to be invalid, with a
     * custom message.
     * 
     * @param message
     *            the custom message.
     */
    public BadSignatureException(String message) {
        super(message);
    }

    /**
     * Signal that a verification signature was found to be invalid, with a
     * cause.
     * 
     * @param cause
     *            the cause.
     */
    public BadSignatureException(Throwable cause) {
        super(cause);
    }
}
