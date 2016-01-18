package org.yi.happy.archive.commandLine;

/**
 * Represents an error in command line parsing. Because the command line is
 * typically provided by the user this is a checked exception, an error expected
 * to need to be checked for in normal operation.
 */
public class CommandParseException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 8090206810674900265L;

    /**
     * Constructs a new exception without a detail message.
     */
    public CommandParseException() {
        super();
    }

    /**
     * Constructs a new exception with a detail message and cause.
     * 
     * @param message
     *            the detail message.
     * @param cause
     *            the cause.
     */
    public CommandParseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with a detail message.
     * 
     * @param message
     *            the detail message.
     */
    public CommandParseException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with a cause.
     * 
     * @param cause
     *            the cause.
     */
    public CommandParseException(Throwable cause) {
        super(cause);
    }
}
