package org.yi.happy.archive.commandLine;

/**
 * A parsing engine for command lines. This one handles named options that take
 * values only. As the options are parsed they are emitted as events.
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
         */
        void onCommand(String command);

        /**
         * emitted when a non-option is found on the command line.
         * 
         * @param argument
         *            the non-option.
         */
        void onArgument(String argument);

        /**
         * emitted when a complete option is found on the command line.
         * 
         * @param name
         *            the name of the option.
         * @param value
         *            the value of the argument to the option.
         */
        void onOption(String name, String value);

        /**
         * emitted when the command line is done being parsed.
         */
        void onFinished();
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
     */
    public void parse(String[] args) {
        for (String arg : args) {
            arg(arg);
        }
        end();
    }

    private void arg(String arg) {
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
                    throw new IllegalArgumentException();
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
                    throw new IllegalArgumentException();
                }

                start = i;

                state = State.NAME;
                continue;
            }

            if (state == State.OPTION2) {
                if (arg.charAt(i) == '-') {
                    throw new IllegalArgumentException();
                }

                if (arg.charAt(i) == '=') {
                    throw new IllegalArgumentException();
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

            throw new IllegalArgumentException();
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

        throw new IllegalArgumentException();
    }

    private void end() {
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

        throw new IllegalArgumentException();
    }
}
