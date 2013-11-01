package org.yi.happy.archive;

/**
 * Indicates there was a run time failure while providing an instance.
 */
public class ProvisionException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -5105077556299349254L;

    /**
     * 
     */
    public ProvisionException() {
        super();
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public ProvisionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public ProvisionException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public ProvisionException(Throwable cause) {
        super(cause);
    }
}
