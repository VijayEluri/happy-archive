package org.yi.happy.archive.block.parser;

/**
 * signal that a key type is not known.
 */
public class UnknownKeyTypeException extends IllegalArgumentException {

    /**
     * 
     */
    private static final long serialVersionUID = -904570696945022977L;

    /**
     * signal that a key type is not known.
     */
    public UnknownKeyTypeException() {
        super();
    }

    /**
     * signal that a key type is not known, with a message and cause.
     * 
     * @param message
     *            the message.
     * @param cause
     *            the cause.
     */
    public UnknownKeyTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * signal that a key type is not known, with a message.
     * 
     * @param message
     *            the message.
     */
    public UnknownKeyTypeException(String message) {
        super(message);
    }

    /**
     * signal that a key type is not known, with a cause.
     * 
     * @param cause
     *            the cause.
     */
    public UnknownKeyTypeException(Throwable cause) {
        super(cause);
    }

}
