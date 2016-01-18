package org.yi.happy.archive;

/**
 * One of the sub-commands.
 */
public interface MainCommand {
    /**
     * Run the sub-command.
     * 
     * @throws Exception
     *             on error.
     */
    public void run() throws Exception;
}
