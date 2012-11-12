package org.yi.happy.archive.commandLine;

public class RequirementBuilder {
    private String[] usesArgs = {};
    private boolean usesStore = false;
    private boolean usesIndex = false;
    private boolean usesNeed = false;

    public RequirementBuilder withUsesStore() {
        usesStore = true;

        return this;
    }

    public RequirementBuilder withUsesIndex() {
        usesIndex = true;

        return this;
    }

    public RequirementBuilder withUsesNeed() {
        usesNeed = true;

        return this;
    }

    public RequirementBuilder withUsesArgs(String... value) {
        usesArgs = value;

        return this;
    }

    public Requirement create() {
        return new Requirement(usesArgs, usesStore, usesIndex, usesNeed);
    }
}
