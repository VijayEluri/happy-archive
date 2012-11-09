package org.yi.happy.archive.commandLine;


public class ArgsPlay {
    public static void main(String[] args) throws CommandLineException {
        Env env = MyEnv.init(args);
        System.out.println(env);
    }

}
