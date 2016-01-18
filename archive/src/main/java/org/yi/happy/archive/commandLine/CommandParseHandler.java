package org.yi.happy.archive.commandLine;

/**
 * The events that are emitted as the command line is parsed.
 */
public interface CommandParseHandler {
    /**
     * emitted when a non-option is found on the command line.
     * 
     * @param argument
     *            the non-option.
     * @throws CommandParseException
     *             if the command line is invalid.
     */
    void onArgument(String argument) throws CommandParseException;

    /**
     * emitted when a complete option is found on the command line.
     * 
     * @param name
     *            the name of the option.
     * @param value
     *            the value of the argument to the option.
     * @throws CommandParseException
     *             if the command line is invalid.
     */
    void onOption(String name, String value) throws CommandParseException;

    /**
     * emitted when the command line is done being parsed.
     * 
     * @throws CommandParseException
     *             if the command line is invalid.
     */
    void onFinished() throws CommandParseException;
}