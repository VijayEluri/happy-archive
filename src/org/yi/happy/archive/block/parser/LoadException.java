package org.yi.happy.archive.block.parser;

/**
 * an error loading a block
 */
public class LoadException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 6358991284416506073L;

    /**
     * Constructs a new loading exception with null as its detail message. The
     * cause is not initialized.
     */
    public LoadException() {
        super();
    }

    /**
     * Constructs a new loading exception with the specified detail message and
     * cause.
     * 
     * @param message
     *            the detail message
     * @param cause
     *            the cause
     */
    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new loading exception with the specified detail message. The
     * cause is not initialized.
     * 
     * @param message
     *            the detail message
     */
    public LoadException(String message) {
        super(message);
    }

    /**
     * Consturcts a new loading exception with the specified cause.
     * 
     * @param cause
     *            the cause
     */
    public LoadException(Throwable cause) {
        super(cause);
    }

}
