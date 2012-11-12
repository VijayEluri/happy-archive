package org.yi.happy.archive.commandLine;

public class RequirementsBuilder {
    private String[] usesArgs = {};
    private boolean usesStore = false;
    private boolean usesIndex = false;
    private boolean usesNeed = false;

    public RequirementsBuilder withUsesStore() {
        usesStore = true;

        return this;
    }

    public RequirementsBuilder withUsesIndex() {
        usesIndex = true;

        return this;
    }

    public RequirementsBuilder withUsesNeed() {
        usesNeed = true;

        return this;
    }

    public RequirementsBuilder withUsesArgs(String... value) {
        usesArgs = value;

        return this;
    }

    public Requirements create() {
        return new Requirements(usesArgs, usesStore, usesIndex, usesNeed);
    }
}
