package org.yi.happy.archive;

import org.yi.happy.annotate.GlobalOutput;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesArgs;

/**
 * Show the invocation environment.
 */
@UsesArgs("...")
@GlobalOutput
public class ShowEnvMain implements MainCommand {

    private final Env env;

    /**
     * set up the command.
     * 
     * @param env
     *            the invocation environment.
     */
    public ShowEnvMain(Env env) {
        this.env = env;
    }

    @Override
    public void run() {
        System.out.println(env);
    }

}
