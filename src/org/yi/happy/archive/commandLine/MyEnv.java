package org.yi.happy.archive.commandLine;

import java.io.File;

public class MyEnv {
    public static Env init(String[] args) throws CommandLineException {
        final EnvBuilder env = new EnvBuilder();

        env.withHome(System.getenv("ARCHIVE_HOME"));

        if (env.hasNoHome()) {
            String home = System.getProperty("user.home");
            if (home != null) {
                env.withHome(home + File.separator + ".archive.d");
            }
        }

        new CommandParseEngine(new CommandParseEngine.CommandParseHandler() {
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
                    throws CommandLineException {
                if (needCommand) {
                    throw new CommandLineException(
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

                throw new CommandLineException("unrecognized option: " + name);
            }

            @Override
            public void onFinished() throws CommandLineException {
                if (needCommand) {
                    throw new CommandLineException(
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
