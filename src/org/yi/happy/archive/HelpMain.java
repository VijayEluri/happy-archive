package org.yi.happy.archive;

import java.io.PrintStream;
import java.util.Map;

/**
 * A command to print out basic help.
 */
public class HelpMain implements MainCommand {
    private final PrintStream out;
    private final Map<String, Class<? extends MainCommand>> commands;

    /**
     * Set up to print out basic help.
     * 
     * @param out
     *            the output stream.
     * @param commands
     *            the command map.
     */
    public HelpMain(PrintStream out,
            Map<String, Class<? extends MainCommand>> commands) {
        this.out = out;
        this.commands = commands;
    }

    @Override
    public void run() throws Exception {
        for (String name : commands.keySet()) {
            out.println(name);
        }
    }
}
