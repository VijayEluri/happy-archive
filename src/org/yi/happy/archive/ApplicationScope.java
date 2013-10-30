package org.yi.happy.archive;

import java.util.List;
import java.util.Map;

import org.yi.happy.archive.commandLine.Env;

/**
 * The application level scope for {@link MyInjector}.
 */
public class ApplicationScope {

    private final Map<String, Class<? extends MainCommand>> commands;
    private final Env env;

    /**
     * Create the application level scope for {@link MyInjector}.
     * 
     * @param commands
     *            the map of available commands to implementations.
     * @param env
     *            the invocation environment.
     */
    public ApplicationScope(Map<String, Class<? extends MainCommand>> commands,
            Env env) {
        this.commands = commands;
        this.env = env;
    }

    /**
     * @return the map of available commands to implementations.
     */
    public Map<String, Class<? extends MainCommand>> getCommands() {
        return commands;
    }

    /**
     * @return the invocation environment.
     */
    public Env getEnv() {
        return env;
    }

    /**
     * @return the value of the store option.
     */
    public String getStore() {
        return env.getStore();
    }

    /**
     * @return the non-option command line arguments.
     */
    public List<String> getArgs() {
        return env.getArguments();
    }

    /**
     * @return the value of the need option.
     */
    public String getNeed() {
        return env.getNeed();
    }

    /**
     * @return the value of the index option.
     */
    public String getIndex() {
        return env.getIndex();
    }
}
