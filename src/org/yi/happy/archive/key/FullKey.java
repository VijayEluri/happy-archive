package org.yi.happy.archive.key;

/**
 * a full key. the data parts vary by type.
 */
public interface FullKey {
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
