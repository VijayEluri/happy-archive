package org.yi.happy.archive.commandLine;

import java.io.File;

public class MyEnv {
    public static Env init(String[] args)
            throws CommandLineException {
        final EnvBuilder env = new EnvBuilder();

        env.withHome(System.getenv("ARCHIVE_HOME"));

        if (env.hasNoHome()) {
            String home = System.getProperty("user.home");
            if (home != null) {
                env.withHome(home + File.separator + ".archive.d");
            }
        }

        new ParseEngine(new ParseEngine.Handler() {
            @Override
            public void onCommand(String command) {
                env.withCommand(command);
            }

            @Override
            public void onArgument(String argument) {
                env.addArgument(argument);
            }

            @Override
            public void onOption(String name, String value) {
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

                throw new IllegalArgumentException();
            }

            @Override
            public void onFinished() {
                // nothing
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
