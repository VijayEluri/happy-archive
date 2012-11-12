package org.yi.happy.archive.commandLine;

/**
 * A builder for {@link Requirement} objects.
 */
public class RequirementBuilder {
    private String[] usesArgs = {};
    private boolean usesStore = false;
    private boolean usesIndex = false;
    private boolean usesNeed = false;
    private String usesInput = null;
    private String usesOutput = null;

    /**
     * set usesStore to true.
     * 
     * @return this.
     */
    public RequirementBuilder withUsesStore() {
        usesStore = true;

        return this;
    }

    /**
     * set usesIndex to true.
     * 
     * @return this.
     */
    public RequirementBuilder withUsesIndex() {
        usesIndex = true;

        return this;
    }

    /**
     * set usesNeed to true.
     * 
     * @return this.
     */
    public RequirementBuilder withUsesNeed() {
        usesNeed = true;

        return this;
    }

    /**
     * set usesArgs to value.
     * 
     * @param value
     *            the new value for usesArgs.
     * @return this.
     */
    public RequirementBuilder withUsesArgs(String... value) {
        usesArgs = value;

        return this;
    }

    /**
     * create the object.
     * 
     * @return the new {@link Requirement} object based on the current state.
     */
    public Requirement create() {
        return new Requirement(usesArgs, usesStore, usesIndex, usesNeed,
                usesInput, usesOutput);
    }

    /**
     * set usesInput to value.
     * 
     * @param value
     *            the value for usesInput.
     * @return this.
     */
    public RequirementBuilder withUsesInput(String value) {
        usesInput = value;

        return this;
    }

    /**
     * set usesOutput to value.
     * 
     * @param value
     *            the value for usesInput.
     * @return this.
     */
    public RequirementBuilder withUsesOutput(String value) {
        usesOutput = value;

        return this;
    }
}
