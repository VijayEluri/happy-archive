package org.yi.happy.archive.commandLine;

import java.io.PrintWriter;
import java.io.Writer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MyArgs {
    private static final String STORE = "store";

    private static final String NEED_LIST = "need-list";

    private static final Options options = new Options()

    .addOption(null, STORE, true, "location of the file store")

    .addOption(null, NEED_LIST, true, "location of the request list");

    private final CommandLine cmd;

    public MyArgs(CommandLine cmd) {
        this.cmd = cmd;
    }

    public String getStore() {
        return cmd.getOptionValue(STORE);
    }
    
    public String getNeedList() {
        return cmd.getOptionValue(NEED_LIST);
    }

    public static MyArgs parse(String[] args) throws CommandLineException {

        CommandLine cmd;
        try {
            cmd = new GnuParser().parse(options, args);
        } catch (ParseException e) {
            throw new CommandLineException(e);
        }

        return new MyArgs(cmd);
    }

    public static void showHelp(Writer out) {
        PrintWriter o = new PrintWriter(out);
        try {
            showHelp0(o);
        } finally {
            o.flush();
        }
    }

    private static void showHelp0(PrintWriter out) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(out, 80, "command", options);
    }

    public static class CommandLineException extends Exception {
        private final ParseException cause;

        public CommandLineException(ParseException cause) {
            super(cause);
            this.cause = cause;
        }

        public void showHelp(Writer out) {
            PrintWriter o = new PrintWriter(out);
            try {
                cause.printStackTrace(o);
                MyArgs.showHelp0(o);
            } finally {
                o.flush();
            }
        }
    }

    public String[] getFiles() {
        return cmd.getArgs();
    }

}
