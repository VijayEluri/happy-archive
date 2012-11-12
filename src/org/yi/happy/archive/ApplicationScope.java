package org.yi.happy.archive;

import java.util.Map;

import org.yi.happy.archive.commandLine.Env;

public class ApplicationScope {

    private final Map<String, Class<? extends MainCommand>> commands;
    private final Env env;

    public ApplicationScope(Map<String, Class<? extends MainCommand>> commands,
            Env env) {
        this.commands = commands;
        this.env = env;
    }

    public Map<String, Class<? extends MainCommand>> getCommands() {
        return commands;
    }

    public Env getEnv() {
        return env;
    }
}
