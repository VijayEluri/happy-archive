package org.yi.happy.archive;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesArgs;

@UsesArgs("...")
public class ShowEnvMain implements MainCommand {

    @Override
    public void run(Env env) {
        System.out.println(env);
    }

}
