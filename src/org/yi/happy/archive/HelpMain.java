package org.yi.happy.archive;

import java.io.PrintStream;
import java.util.Set;

import org.yi.happy.archive.commandLine.Env;

public class HelpMain implements MainCommand {
    private final PrintStream out;
    private final Set<String> commandNames;

    public HelpMain(PrintStream out, Set<String> commandNames) {
        this.out = out;
        this.commandNames = commandNames;
    }

    @Override
    public void run(Env env) throws Exception {
        for (String name : commandNames) {
            out.println(name);
        }
    }
}
