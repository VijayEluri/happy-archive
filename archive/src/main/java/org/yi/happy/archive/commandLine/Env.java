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
    private final String blockStore;
    private final String indexStore;
    private final String need;
    private final String command;
    private final List<String> arguments;

    /**
     * Create an invocation environment value.
     * 
     * @param home
     *            the archive data home directory.
     * @param blockStore
     *            the base of the block store.
     * @param indexStore
     *            the base of the index store.
     * @param need
     *            the needed block list file name.
     * @param command
     *            the command being invoked.
     * @param arguments
     *            the rest of the arguments.
     */
    public Env(String home, String blockStore, String indexStore, String need,
            String command, List<String> arguments) {
        this.home = home;
        this.blockStore = blockStore;
        this.indexStore = indexStore;
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
    public String getBlockStore() {
        return blockStore;
    }

    /**
     * @return the location of the index store.
     */
    public String getIndexStore() {
        return indexStore;
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
        return "Env [home=" + home + ", blockStore=" + blockStore
                + ", indexStore=" + indexStore + ", need=" + need
                + ", command=" + command + ", arguments=" + arguments + "]";
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
    public boolean hasNoBlockStore() {
        return blockStore == null;
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
    public boolean hasNoIndexStore() {
        return indexStore == null;
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
        result = prime * result
                + ((indexStore == null) ? 0 : indexStore.hashCode());
        result = prime * result + ((need == null) ? 0 : need.hashCode());
        result = prime * result
                + ((blockStore == null) ? 0 : blockStore.hashCode());
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
        if (indexStore == null) {
            if (other.indexStore != null)
                return false;
        } else if (!indexStore.equals(other.indexStore))
            return false;
        if (need == null) {
            if (other.need != null)
                return false;
        } else if (!need.equals(other.need))
            return false;
        if (blockStore == null) {
            if (other.blockStore != null)
                return false;
        } else if (!blockStore.equals(other.blockStore))
            return false;
        return true;
    }
}
