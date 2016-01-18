package org.yi.happy.archive.block.parser;

/**
 * Signals missing meta data.
 */
public class MissingMetaException extends IllegalArgumentException {

    /**
     * 
     */
    private static final long serialVersionUID = -6231320154182629358L;

    /**
     * signal missing meta data.
     */
    public MissingMetaException() {
        super();
    }

    /**
     * signal missing meta data, with a message and cause.
     * 
     * @param message
     *            the message.
     * @param cause
     *            the cause.
     */
    public MissingMetaException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * signal missing meta data, with a message.
     * 
     * @param message
     *            the message.
     */
    public MissingMetaException(String message) {
        super(message);
    }

    /**
     * signal missing meta data, with a cause.
     * 
     * @param cause
     *            the cause.
     */
    public MissingMetaException(Throwable cause) {
        super(cause);
    }

}
