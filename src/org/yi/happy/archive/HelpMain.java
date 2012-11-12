package org.yi.happy.archive;

import java.io.PrintStream;
import java.util.Map;

import org.yi.happy.archive.commandLine.Env;

public class HelpMain implements MainCommand {
    private final PrintStream out;
    private final Map<String, Class<? extends MainCommand>> commands;

    public HelpMain(PrintStream out,
            Map<String, Class<? extends MainCommand>> commands) {
        this.out = out;
        this.commands = commands;
    }

    @Override
    public void run(Env env) throws Exception {
        for (String name : commands.keySet()) {
            out.println(name);
        }
    }
}
