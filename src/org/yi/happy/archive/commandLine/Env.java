package org.yi.happy.archive.commandLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The invocation environment value object. This should be all that is needed
 * from the environment variables and command line.
 */
public class Env {
    private final String home;
    private final String store;
    private final String index;
    private final String need;
    private final String command;
    private final List<String> arguments;

    /**
     * Create an invocation environment value.
     * 
     * @param home
     *            the archive data home directory.
     * @param store
     *            the base of the block store.
     * @param index
     *            the base of the index store.
     * @param need
     *            the needed block list file name.
     * @param command
     *            the command being invoked.
     * @param arguments
     *            the rest of the arguments.
     */
    public Env(String home, String store, String index, String need,
            String command, List<String> arguments) {
        this.home = home;
        this.store = store;
        this.index = index;
        this.need = need;
        this.command = command;

        this.arguments = Collections.unmodifiableList(new ArrayList<String>(
                arguments));
    }

    public String getHome() {
        return home;
    }

    public String getStore() {
        return store;
    }

    public String getIndex() {
        return index;
    }

    public String getNeed() {
        return need;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "Env [home=" + home + ", store=" + store + ", index=" + index
                + ", need=" + need + ", command=" + command + ", arguments="
                + arguments + "]";
    }

    public boolean hasArguments() {
        return !arguments.isEmpty();
    }

    public boolean hasNoStore() {
        return store == null;
    }

    public boolean hasNoNeed() {
        return need == null;
    }

    public boolean hasNoArguments() {
        return arguments.isEmpty();
    }
}
