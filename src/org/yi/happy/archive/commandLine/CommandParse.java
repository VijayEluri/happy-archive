package org.yi.happy.archive.commandLine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Play with implementing my own command line parsing.
 */
public class CommandParse {
    /**
     * the sample playground.
     * 
     * @param args
     * @throws CommandLineException
     */
    public static void main(String[] args) throws CommandLineException {
        args = new String[] { "tag-put", "--store", "archive.d/store",
                "file.txt", "--index=archive.d/index" };

        CommandParse cl = new CommandParse();
        cl.parse(args);

        System.out.println(cl.getCmd());
        System.out.println(cl.getStore());
        System.out.println(cl.getIndex());
        System.out.println(cl.getNeed());
        System.out.println(cl.getFiles());
    }

    private Set<String> commands;

    /**
     * set up the object.
     */
    public CommandParse() {
        files = new ArrayList<String>();

        commands = new HashSet<String>();
        commands.add("tag-put");
    }

    /**
     * parse the command line.
     * 
     * @param args
     *            the command line.
     * @throws CommandLineException
     */
    public void parse(String[] args) throws CommandLineException {
        new CommandParseEngine(new CommandParseHandler() {
            boolean needCommand = true;

            @Override
            public void onArgument(String argument) {
                if (needCommand) {
                    if (!commands.contains(argument)) {
                        throw new IllegalArgumentException();
                    }

                    CommandParse.this.command = argument;
                    needCommand = false;
                    return;
                }

                files.add(argument);
            }

            @Override
            public void onOption(String name, String value) {
                if (name.equals("store")) {
                    store = value;
                    return;
                }

                if (name.equals("index")) {
                    index = value;
                    return;
                }

                if (name.equals("need")) {
                    need = value;
                    return;
                }

                throw new IllegalArgumentException();
            }

            @Override
            public void onFinished() {
                // nothing
            }
        }).parse(args);
    }

    private String command;
    private String store;
    private String index;
    private String need;
    private List<String> files;

    /**
     * get the name of the command on the command line.
     * 
     * @return the command on the command line.
     */
    public String getCmd() {
        return command;
    }

    /**
     * 
     * @return the path to the block store.
     */
    public String getStore() {
        return store;
    }

    /**
     * @return the path to the index store.
     */
    public String getIndex() {
        return index;
    }

    /**
     * @return the path to the needed block list.
     */
    public String getNeed() {
        return need;
    }

    /**
     * @return the non-options on the command line.
     */
    public List<String> getFiles() {
        return files;
    }
}
