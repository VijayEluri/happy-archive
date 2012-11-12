package org.yi.happy.archive.commandLine;

public class Requirements {
    private final String[] usesArgs;
    private final boolean usesStore;
    private final boolean usesIndex;
    private final boolean usesNeed;

    public Requirements(String[] usesArgs, boolean usesStore,
            boolean usesIndex, boolean usesNeed) {
        this.usesArgs = usesArgs;
        this.usesStore = usesStore;
        this.usesIndex = usesIndex;
        this.usesNeed = usesNeed;
    }

    public String[] getUsesArgs() {
        return usesArgs;
    }

    public boolean getUsesStore() {
        return usesStore;
    }

    public boolean getUsesIndex() {
        return usesIndex;
    }

    public boolean getUsesNeed() {
        return usesNeed;
    }

    public boolean isVarArgs() {
        return (usesArgs.length > 0 && usesArgs[usesArgs.length - 1]
                .endsWith("..."));
    }

    public int getMinArgs() {
        if (isVarArgs()) {
            return usesArgs.length - 1;
        }

        return usesArgs.length;
    }
}
