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

    /**
     * @return the base data directory.
     */
    public String getHome() {
        return home;
    }

    /**
     * @return the location of the block store.
     */
    public String getStore() {
        return store;
    }

    /**
     * @return the location of the index store.
     */
    public String getIndex() {
        return index;
    }

    /**
     * @return the location of the request list file.
     */
    public String getNeed() {
        return need;
    }

    /**
     * @return the name of the command.
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return all the arguments.
     */
    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "Env [home=" + home + ", store=" + store + ", index=" + index
                + ", need=" + need + ", command=" + command + ", arguments="
                + arguments + "]";
    }

    /**
     * @return true if there are any arguments.
     */
    public boolean hasArguments() {
        return !arguments.isEmpty();
    }

    /**
     * @return true if there is no store set.
     */
    public boolean hasNoStore() {
        return store == null;
    }

    /**
     * @return true if there is no request list file name set.
     */
    public boolean hasNoNeed() {
        return need == null;
    }

    /**
     * @return true if there are no arguments.
     */
    public boolean hasNoArguments() {
        return arguments.isEmpty();
    }

    /**
     * @return the number of arguments.
     */
    public int hasArgumentCount() {
        return arguments.size();
    }

    /**
     * @return true if there is no index set.
     */
    public boolean hasNoIndex() {
        return index == null;
    }

    /**
     * @param index
     *            which argument to fetch.
     * @return the argument at that position.
     */
    public String getArgument(int index) {
        return arguments.get(index);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((arguments == null) ? 0 : arguments.hashCode());
        result = prime * result + ((command == null) ? 0 : command.hashCode());
        result = prime * result + ((home == null) ? 0 : home.hashCode());
        result = prime * result + ((index == null) ? 0 : index.hashCode());
        result = prime * result + ((need == null) ? 0 : need.hashCode());
        result = prime * result + ((store == null) ? 0 : store.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Env other = (Env) obj;
        if (arguments == null) {
            if (other.arguments != null)
                return false;
        } else if (!arguments.equals(other.arguments))
            return false;
        if (command == null) {
            if (other.command != null)
                return false;
        } else if (!command.equals(other.command))
            return false;
        if (home == null) {
            if (other.home != null)
                return false;
        } else if (!home.equals(other.home))
            return false;
        if (index == null) {
            if (other.index != null)
                return false;
        } else if (!index.equals(other.index))
            return false;
        if (need == null) {
            if (other.need != null)
                return false;
        } else if (!need.equals(other.need))
            return false;
        if (store == null) {
            if (other.store != null)
                return false;
        } else if (!store.equals(other.store))
            return false;
        return true;
    }
}
