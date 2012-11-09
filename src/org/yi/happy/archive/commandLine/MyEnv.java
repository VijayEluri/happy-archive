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

        new CommandParseEngine(new CommandParseHandler() {
            private boolean needCommand = true;

            @Override
            public void onArgument(String argument) {
                if (needCommand) {
                    env.withCommand(argument);
                    needCommand = false;
                    return;
                }
                env.addArgument(argument);
            }

            @Override
            public void onOption(String name, String value)
                    throws CommandParseException {
                if (needCommand) {
                    throw new CommandParseException(
                            "the first argument must be a command");
                }

                if (name.equals("archive-home")) {
                    env.withHome(value);
                    return;
                }

                if (name.equals("store")) {
                    env.withStore(value);
                    return;
                }

                if (name.equals("index")) {
                    env.withIndex(value);
                    return;
                }

                if (name.equals("need")) {
                    env.withNeed(value);
                    return;
                }

                throw new CommandParseException("unrecognized option: " + name);
            }

            @Override
            public void onFinished() throws CommandParseException {
                if (needCommand) {
                    throw new CommandParseException(
                            "the first argument must be a command");
                }
            }
        }).parse(args);

        if (env.hasNoStore() && env.hasHome()) {
            env.withStore(env.getHome() + File.separator + "store");
        }

        if (env.hasNoIndex() && env.hasHome()) {
            env.withIndex(env.getHome() + File.separator + "index");
        }

        return env.create();
    }
}
