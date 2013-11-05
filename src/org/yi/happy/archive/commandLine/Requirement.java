package org.yi.happy.archive.commandLine;

/**
 * The invocation environment requirements for a command. This was created to be
 * used to verify the invocation environment without instantiating the object to
 * run the command, and display command usage.
 */
public class Requirement {
    private final String[] usesArgs;
    private final boolean usesStore;
    private final boolean usesIndex;
    private final boolean usesNeed;
    private final String usesInput;
    private final String usesOutput;

    /**
     * Create an invocation environment requirement description.
     * 
     * @param usesArgs
     *            the command line arguments used.
     * @param usesStore
     *            true if the store option is used.
     * @param usesIndex
     *            true if the index option is used.
     * @param usesNeed
     *            true if the need option is used.
     * @param usesInput
     * @param usesOutput
     */
    public Requirement(String[] usesArgs, boolean usesStore,
 boolean usesIndex,
            boolean usesNeed, String usesInput, String usesOutput) {
        this.usesArgs = usesArgs;
        this.usesStore = usesStore;
        this.usesIndex = usesIndex;
        this.usesNeed = usesNeed;
        this.usesInput = usesInput;
        this.usesOutput = usesOutput;
    }

    /**
     * @return the command line arguments used.
     */
    public String[] getUsesArgs() {
        return usesArgs;
    }

    /**
     * @return true if the store option is used.
     */
    public boolean getUsesBlockStore() {
        return usesStore;
    }

    /**
     * @return true if the index option is used.
     */
    public boolean getUsesIndexStore() {
        return usesIndex;
    }

    /**
     * @return true if the need option is used.
     */
    public boolean getUsesNeed() {
        return usesNeed;
    }

    /**
     * @return the name of the input part of the command description.
     */
    public String getUsesInput() {
        return usesInput;
    }

    /**
     * @return the name of the output part of the command description.
     */
    public String getUsesOutput() {
        return usesOutput;
    }

    /**
     * @return true if the final argument repeats.
     */
    public boolean isVarArgs() {
        if (usesArgs.length < 1) {
            return false;
        }

        return usesArgs[usesArgs.length - 1].endsWith("...");
    }

    /**
     * @return the minimum number of arguments required.
     */
    public int getMinArgs() {
        if (isVarArgs()) {
            return usesArgs.length - 1;
        }

        return usesArgs.length;
    }
}
