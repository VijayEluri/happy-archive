package org.yi.happy.archive;

/**
 * thrown when a verify fails
 */
public class VerifyException extends IllegalArgumentException {

    /**
     * 
     */
    private static final long serialVersionUID = -2724017561229270621L;

    /**
     * create with no message
     */
    public VerifyException() {
    }

    /**
     * create with a message
     * 
     * @param message
     */
    public VerifyException(String message) {
        super(message);
    }

    /**
     * create with no message and a cause
     * 
     * @param cause
     */
    public VerifyException(Throwable cause) {
        super(cause);
    }

    /**
     * create with a message and cause
     * 
     * @param message
     * @param cause
     */
    public VerifyException(String message, Throwable cause) {
        super(message, cause);
    }

}
