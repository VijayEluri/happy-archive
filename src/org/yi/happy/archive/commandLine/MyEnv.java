package org.yi.happy.archive.commandLine;

import java.io.File;

/**
 * Initialize the environment using some reasonable defaults from environment
 * variables and the command line.
 */
public class MyEnv {
    /**
     * Initialize the environment using some reasonable defaults from
     * environment variables and the command line.
     * 
     * @param args
     *            the arguments.
     * @return the constructed environment.
     * @throws CommandParseException
     *             on errors.
     */
    public static Env init(String[] args) throws CommandParseException {
        final EnvBuilder env = new EnvBuilder();

        env.withHome(System.getenv("ARCHIVE_HOME"));

        if (env.hasNoHome()) {
            String home = System.getProperty("user.home");
            if (home != null) {
                env.withHome(home + File.separator + ".archive.d");
            }
        }

        new CommandParseEngine(new EnvHandler(env)).parse(args);

        if (env.hasNoBlockStore() && env.hasHome()) {
            env.withBlockStore(env.getHome() + File.separator + "block");
        }

        if (env.hasNoIndexStore() && env.hasHome()) {
            env.withIndexStore(env.getHome() + File.separator + "index");
        }

        return env.create();
    }
}
