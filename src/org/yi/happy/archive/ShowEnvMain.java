package org.yi.happy.archive;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesArgs;

@UsesArgs("...")
public class ShowEnvMain implements MainCommand {

    private final Env env;

    public ShowEnvMain(Env env) {
        this.env = env;
    }

    @Override
    public void run() {
        System.out.println(env);
    }

}
