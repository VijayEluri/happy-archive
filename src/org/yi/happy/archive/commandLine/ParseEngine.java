package org.yi.happy.archive.commandLine;

/**
 * A parsing engine for command lines. This one handles named options that take
 * values only. As the options are parsed they are emitted as events. This
 * parser requires that the first item on the command line is a command name.
 */
public class ParseEngine {
    /**
     * The events that are emitted as the command line is parsed.
     */
    public interface Handler {
        /**
         * emitted when the initial command is found on the command line.
         * 
         * @param command
         *            the command that was found on the command line.
         * @throws CommandLineException
         *             if the command line is invalid.
         */
        void onCommand(String command) throws CommandLineException;

        /**
         * emitted when a non-option is found on the command line.
         * 
         * @param argument
         *            the non-option.
         * @throws CommandLineException
         *             if the command line is invalid.
         */
        void onArgument(String argument) throws CommandLineException;

        /**
         * emitted when a complete option is found on the command line.
         * 
         * @param name
         *            the name of the option.
         * @param value
         *            the value of the argument to the option.
         * @throws CommandLineException
         *             if the command line is invalid.
         */
        void onOption(String name, String value) throws CommandLineException;

        /**
         * emitted when the command line is done being parsed.
         * 
         * @throws CommandLineException
         *             if the command line is invalid.
         */
        void onFinished() throws CommandLineException;
    }

    /**
     * the name of the option currently being parsed.
     */
    private String name;

    /**
     * where to emit events to.
     */
    private final Handler handler;

    private enum State {
        START, FILE, OPTION, OPTION2, NAME, VALUE, DONE
    };

    private State state;

    /**
     * Set up the parsing engine.
     * 
     * @param handler
     *            where to emit events to.
     */
    public ParseEngine(Handler handler) {
        this.handler = handler;

        state = State.START;
    }

    /**
     * Parse command line arguments. This should only be called once.
     * 
     * @param args
     *            the arguments.
     * @throws CommandLineException
     *             if the command line is invalid.
     */
    public void parse(String[] args) throws CommandLineException {
        for (String arg : args) {
            arg(arg);
        }
        end();
    }

    private void arg(String arg) throws CommandLineException {
        /*
         * start of argument
         */

        if (state == State.OPTION) {
            throw new IllegalStateException();
        }

        if (state == State.OPTION2) {
            throw new IllegalStateException();
        }

        if (state == State.NAME) {
            throw new IllegalStateException();
        }

        if (state == State.VALUE) {
            handler.onOption(name, arg);

            state = State.FILE;
            return;
        }

        if (state == State.DONE) {
            handler.onArgument(arg);

            return;
        }

        /*
         * content of argument
         */

        int start = 0;
        for (int i = 0; i < arg.length(); i++) {
            if (state == State.START) {
                if (arg.charAt(i) == '-') {
                    throw new CommandLineException(
                            "the first argument must be a command name");
                }

                handler.onCommand(arg);

                state = State.FILE;
                return;
            }

            if (state == State.FILE) {
                if (arg.charAt(i) == '-') {
                    state = State.OPTION;
                    continue;
                }

                handler.onArgument(arg);

                state = State.FILE;
                return;
            }

            if (state == State.OPTION) {
                if (arg.charAt(i) == '-') {
                    state = State.OPTION2;
                    continue;
                }

                if (arg.charAt(i) == '=') {
                    throw new CommandLineException(
                            "The option must have a name");
                }

                start = i;

                state = State.NAME;
                continue;
            }

            if (state == State.OPTION2) {
                if (arg.charAt(i) == '-') {
                    throw new CommandLineException(
                            "The option can not begin with three dashes");
                }

                if (arg.charAt(i) == '=') {
                    throw new CommandLineException(
                            "The option must have a name");
                }

                start = i;

                state = State.NAME;
                continue;
            }

            if (state == State.NAME) {
                if (arg.charAt(i) == '=') {
                    name = arg.substring(start, i);
                    start = i + 1;

                    state = State.VALUE;
                    break;
                }

                state = State.NAME;
                continue;
            }

            throw new IllegalStateException();
        }

        /*
         * end of argument
         */

        if (state == State.FILE) {
            handler.onArgument(arg);

            state = State.FILE;
            return;
        }

        if (state == State.OPTION) {
            handler.onArgument(arg);

            state = State.FILE;
            return;
        }

        if (state == State.OPTION2) {
            state = State.DONE;
            return;
        }

        if (state == State.NAME) {
            name = arg.substring(start);

            state = State.VALUE;
            return;
        }

        if (state == State.VALUE) {
            String value = arg.substring(start);
            handler.onOption(name, value);
            name = null;

            state = State.FILE;
            return;
        }

        throw new IllegalStateException();
    }

    private void end() throws CommandLineException {
        if (state == State.FILE) {
            handler.onFinished();

            state = State.DONE;
            return;
        }

        if (state == State.DONE) {
            handler.onFinished();

            state = State.DONE;
            return;
        }

        if (state == State.VALUE) {
            throw new CommandLineException("The option must have a value");
        }

        if(state == State.START) {
            throw new CommandLineException(
                    "The first argument must be a command name");
        }
        
        throw new IllegalStateException();
    }
}
