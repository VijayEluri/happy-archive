package org.yi.happy.archive.key;

/**
 * the base for any type of key.
 */
public interface Key {
    /**
     * @return the type of the key
     */
    public String getType();

    /**
     * @return the string that parses into this key
     */
    public String toString();

    /**
     * Get the locator key for this key.
     * 
     * @return A locator key.
     */
    public LocatorKey toLocatorKey();

    public int hashCode();

    public boolean equals(Object obj);
}
