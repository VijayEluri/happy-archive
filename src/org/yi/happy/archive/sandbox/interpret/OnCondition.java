package org.yi.happy.archive.sandbox.interpret;

/**
 * A condition that can be used in a rule.
 */
public interface OnCondition {

    /**
     * Check if this condition applies to the startStream event.
     * 
     * @return true if this applies to the startStream event.
     */
    boolean startStream();

    /**
     * Check if this condition applies to the endStream event.
     * 
     * @return true if this applies to the endStream event.
     */
    boolean endStream();

    /**
     * Check if this condition applies to the data event.
     * 
     * @param b
     *            the data byte.
     * @return true if this applies to the data event.
     */
    boolean data(byte b);

    /**
     * Check if this condition applies to the startRegion event.
     * 
     * @param name
     *            the name of the region.
     * @return true if this applies to the startRegion event.
     */
    boolean startRegion(String name);

    /**
     * Check if this condition applies to the endRegion event.
     * 
     * @param name
     *            the name of the region.
     * @return true if this applies to the endRegion event.
     */
    boolean endRegion(String name);
}
