package org.yi.happy.archive.commandLine;

import java.io.PrintWriter;
import java.io.Writer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * The command line arguments for the main entry point.
 */
public class MyArgs {
    private static final String STORE = "store";

    private static final String NEED_LIST = "need-list";

    private static final Options options = new Options()

    .addOption(null, STORE, true, "location of the file store")

    .addOption(null, NEED_LIST, true, "location of the request list");

    private final CommandLine cmd;

    /**
     * set up the command line arguments for the main entry point, called from
     * parse.
     * 
     * @param cmd
     *            the parsed command line.
     */
    public MyArgs(CommandLine cmd) {
        this.cmd = cmd;
    }

    /**
     * get the store option.
     * 
     * @return the store option.
     */
    public String getStore() {
        return cmd.getOptionValue(STORE);
    }

    /**
     * get the need list option value.
     * 
     * @return the need list option value.
     */
    public String getNeedList() {
        return cmd.getOptionValue(NEED_LIST);
    }

    /**
     * parse the command line.
     * 
     * @param args
     *            the command line.
     * @return the parsed command line.
     * @throws CommandLineException
     *             on error.
     */
    public static MyArgs parse(String[] args) throws CommandLineException {

        CommandLine cmd;
        try {
            cmd = new GnuParser().parse(options, args);
        } catch (ParseException e) {
            throw new CommandLineException(e);
        }

        return new MyArgs(cmd);
    }

    /**
     * print out the command line help.
     * 
     * @param out
     *            the writer to print on.
     */
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

    /**
     * an error from parsing the command line.
     */
    public static class CommandLineException extends Exception {
        /**
         * 
         */
        private static final long serialVersionUID = 5586604964781859405L;
        private final ParseException cause;

        /**
         * make an error from parsing the command line, from a parse exception
         * cause.
         * 
         * @param cause
         *            the parse exception.
         */
        public CommandLineException(ParseException cause) {
            super(cause);
            this.cause = cause;
        }

        /**
         * show the command line help for this error.
         * 
         * @param out
         *            where to display the help.
         */
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

    /**
     * get the file arguments from the command line.
     * 
     * @return the file arguments.
     */
    public String[] getFiles() {
        return cmd.getArgs();
    }

}