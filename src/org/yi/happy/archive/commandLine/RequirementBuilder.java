package org.yi.happy.archive.commandLine;

public class RequirementBuilder {
    private String[] usesArgs = {};
    private boolean usesStore = false;
    private boolean usesIndex = false;
    private boolean usesNeed = false;
    private String usesInput = null;
    private String usesOutput = null;

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
        return new Requirement(usesArgs, usesStore, usesIndex, usesNeed,
                usesInput, usesOutput);
    }

    public void withUsesInput(String value) {
        usesInput = value;
        // TODO Auto-generated method stub

    }

    public void withUsesOutput(String value) {
        usesOutput = value;
        // TODO Auto-generated method stub

    }
}
