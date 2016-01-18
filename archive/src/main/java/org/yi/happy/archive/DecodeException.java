package org.yi.happy.archive;

import java.io.IOException;

/**
 * signals a failure to decode when trying to do an IO operation.
 */
public class DecodeException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = 786463010291786940L;

    /**
     * signals a failure to decode when trying to do an IO operation.
     */
    public DecodeException() {
    }

    /**
     * signals a failure to decode when trying to do an IO operation, with a
     * custom message and a cause.
     * 
     * @param message
     *            the custom message.
     * @param cause
     *            the cause.
     */
    public DecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * signals a failure to decode when trying to do an IO operation, with a
     * custom message.
     * 
     * @param message
     *            the custom message.
     */
    public DecodeException(String message) {
        super(message);
    }

    /**
     * signals a failure to decode when trying to do an IO operation, with a
     * cause.
     * 
     * @param cause
     *            the cause.
     */
    public DecodeException(Throwable cause) {
        super(cause);
    }

}
