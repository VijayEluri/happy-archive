package org.yi.happy.archive.commandLine;

/**
 * A parsing engine for command lines. This one handles named options that take
 * values only. As the options are parsed they are emitted as events.
 */
public class ParseEngine {
    /**
     * The events that are emitted as the command line is parsed.
     */
    public interface Visitor {
        /**
         * emitted when the initial command is found on the command line.
         * 
         * @param arg
         *            the command that was found on the command line.
         */
        void command(String arg);

        /**
         * emitted when a non-option is found on the command line.
         * 
         * @param arg
         *            the non-option.
         */
        void file(String arg);

        /**
         * emitted when a complete option is found on the command line.
         * 
         * @param name
         *            the name of the option.
         * @param value
         *            the value of the argument to the option.
         */
        void option(String name, String value);

    }

    private State state;
    private String optionName;
    private final Visitor visitor;

    private enum State {
        START, FILE, OPTION, OPTION2, NAME, VALUE, DONE
    };

    /**
     * Set up the parsing engine.
     * 
     * @param visitor
     *            where to emit events to.
     */
    public ParseEngine(Visitor visitor) {
        this.visitor = visitor;

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
            visitor.option(optionName, arg);
            state = State.FILE;
            return;
        }

        if (state == State.DONE) {
            visitor.file(arg);
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

                visitor.command(arg);
                state = State.FILE;
                return;
            }

            if (state == State.FILE) {
                if (arg.charAt(i) == '-') {
                    state = State.OPTION;
                    continue;
                }

                visitor.file(arg);
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
                    optionName = arg.substring(start, i);
                    start = i + 1;
                    state = State.VALUE;
                    continue;
                }

                continue;
            }

            if (state == State.VALUE) {
                continue;
            }

            throw new IllegalArgumentException();
        }

        /*
         * end of argument
         */

        if (state == State.FILE) {
            visitor.file(arg);
            state = State.FILE;
            return;
        }

        if (state == State.OPTION) {
            visitor.file(arg);
            state = State.FILE;
            return;
        }

        if (state == State.OPTION2) {
            state = State.DONE;
            return;
        }

        if (state == State.NAME) {
            optionName = arg.substring(start);
            state = State.VALUE;
            return;
        }

        if (state == State.VALUE) {
            String optionValue = arg.substring(start);
            visitor.option(optionName, optionValue);
            optionName = null;
            state = State.FILE;
            return;
        }

        throw new IllegalArgumentException();
    }

    private void end() {
        if (state == State.FILE) {
            state = State.DONE;
            return;
        }

        if (state == State.DONE) {
            return;
        }

        throw new IllegalArgumentException();
    }
}
