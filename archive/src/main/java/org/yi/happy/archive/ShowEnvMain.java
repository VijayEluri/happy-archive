package org.yi.happy.archive;

import java.io.PrintStream;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesOutput;

/**
 * Show the invocation environment.
 */
@UsesArgs("...")
@UsesOutput("env")
public class ShowEnvMain implements MainCommand {

    private final Env env;
    private final PrintStream out;

    /**
     * set up the command.
     * 
     * @param env
     *            the invocation environment.
     * @param out
     *            where to print the output.
     */
    public ShowEnvMain(Env env, PrintStream out) {
        this.env = env;
        this.out = out;
    }

    @Override
    public void run() {
        out.println(env);
    }

}
