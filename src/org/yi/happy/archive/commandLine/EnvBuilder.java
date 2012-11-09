package org.yi.happy.archive.commandLine;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder for {@link Env} objects.
 */
public class EnvBuilder {
    private String command = null;

    /**
     * build the {@link Env} object with the given command.
     * 
     * @param command
     *            the command to use.
     * @return this
     */
    public EnvBuilder withCommand(String command) {
        this.command = command;
        return this;
    }
    
    private String home = null;

    /**
     * build the {@link Env} object with the given base directory.
     * 
     * @param home
     *            the base directory to use.
     * @return this
     */
    public EnvBuilder withHome(String home) {
        this.home = home;
        return this;
    }

    /**
     * @return true if home has not been set.
     */
    public boolean hasNoHome() {
        return home == null;
    }

    private String store = null;

    /**
     * build the {@link Env} object with the given store.
     * 
     * @param store
     *            the store to use.
     * @return this
     */
    public EnvBuilder withStore(String store) {
        this.store = store;
        return this;
    }

    private String index = null;

    /**
     * build the {@link Env} object with the given index.
     * 
     * @param index
     *            the index to use.
     * @return this
     */
    public EnvBuilder withIndex(String index) {
        this.index = index;
        return this;
    }

    private String need = null;

    /**
     * build the {@link Env} object with the given need list.
     * 
     * @param need
     *            the need list to use.
     * @return this
     */
    public EnvBuilder withNeed(String need) {
        this.need = need;
        return this;
    }

    private List<String> arguments = new ArrayList<String>();

    /**
     * add the given argument to the list of arguments in the resulting
     * {@link Env} object.
     * 
     * @param argument
     *            the argument to add.
     * @return this
     */
    public EnvBuilder addArgument(String argument) {
        arguments.add(argument);
        return this;
    }

    /**
     * Create the {@link Env} object from all the accumulated information.
     * 
     * @return the resulting {@link Env} object.
     */
    public Env create() {
        return new Env(home, store, index, need, command, arguments);
    }

    /**
     * @return true if home has been set.
     */
    public boolean hasHome() {
        return home != null;
    }

    /**
     * @return true if home has not been set.
     */
    public boolean hasNoStore() {
        return store == null;
    }

    /**
     * @return the value that has been provided for home.
     */
    public String getHome() {
        return home;
    }

    /**
     * @return true if index has not been set.
     */
    public boolean hasNoIndex() {
        return index == null;
    }

}
