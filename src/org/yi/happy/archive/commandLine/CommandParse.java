package org.yi.happy.archive.commandLine;

import java.util.ArrayList;
import java.util.List;

public class CommandParse {
    public static void main(String[] args) {
        args = new String[] { "tag-put", "--store", "archive.d/store",
                "file.txt", "--index=archive.d/index" };

        CommandParse cl = new CommandParse();
        for (String arg : args) {
            cl.arg(arg);
        }
        cl.end();

        System.out.println(cl.getCmd());
        System.out.println(cl.getStore());
        System.out.println(cl.getIndex());
        System.out.println(cl.getFiles());
    }

    private String cmd;
    private String store;
    private String index;
    private State state;
    private List<String> files;
    private String optionName;

    enum State {
        START, FILE, OPTION, OPTION2, NAME, VALUE, DONE
    };

    public CommandParse() {
        files = new ArrayList<String>();
        state = State.START;
    }

    public void arg(String arg) {
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
            option(optionName, arg);
            state = State.FILE;
            return;
        }

        if (state == State.DONE) {
            files.add(arg);
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

                files.add(arg);
                return;
            }

            if (state == State.START) {
                if (arg.charAt(i) == '-') {
                    throw new IllegalArgumentException();
                }

                cmd = arg;
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
            files.add(arg);
            state = State.FILE;
            return;
        }

        if (state == State.OPTION) {
            files.add(arg);
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
            option(optionName, optionValue);
            optionName = null;
            state = State.FILE;
            return;
        }

        throw new IllegalArgumentException();
    }

    private void option(String name, String value) {
        if (name.equals("store")) {
            store = value;
            return;
        }

        if (name.equals("index")) {
            index = value;
            return;
        }

        throw new IllegalArgumentException();
    }

    public void end() {
        if (state == State.FILE) {
            state = State.DONE;
            return;
        }

        if (state == State.DONE) {
            return;
        }

        throw new IllegalArgumentException();
    }

    public String getCmd() {
        return cmd;
    }

    public String getStore() {
        return store;
    }

    public String getIndex() {
        return index;
    }

    public List<String> getFiles() {
        return files;
    }
}
