package org.yi.happy.archive.commandLine;

/**
 * A parsing engine for command lines. This one handles named options that take
 * values only. As the options are parsed they are emitted as events.
 */
public class CommandParseEngine {
    /**
     * the name of the option currently being parsed.
     */
    private String name;

    /**
     * where to emit events to.
     */
    private final CommandParseHandler handler;

    private enum State {
        FILE, OPTION, OPTION2, NAME, VALUE, DONE
    };

    private State state;

    /**
     * Set up the parsing engine.
     * 
     * @param handler
     *            where to emit events to.
     */
    public CommandParseEngine(CommandParseHandler handler) {
        this.handler = handler;

        state = State.FILE;
    }

    /**
     * Parse command line arguments. This should only be called once.
     * 
     * @param args
     *            the arguments.
     * @throws CommandParseException
     *             if the command line is invalid.
     */
    public void parse(String... args) throws CommandParseException {
        for (String arg : args) {
            arg(arg);
        }
        end();
    }

    private void arg(String arg) throws CommandParseException {
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
                    throw new CommandParseException(
                            "The option must have a name");
                }

                start = i;

                state = State.NAME;
                continue;
            }

            if (state == State.OPTION2) {
                if (arg.charAt(i) == '-') {
                    throw new CommandParseException(
                            "The option can not begin with three dashes");
                }

                if (arg.charAt(i) == '=') {
                    throw new CommandParseException(
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

        throw new IllegalStateException("" + state);
    }

    private void end() throws CommandParseException {
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
            throw new CommandParseException("The option must have a value");
        }

        throw new IllegalStateException();
    }
}
