package org.yi.happy.archive.key;


/**
 * a locator key, a reference to a raw block. there are no more data parts than
 * just the type and hash.
 */
public interface LocatorKey extends Comparable<LocatorKey> {
    /**
     * 
     * @return the hash. must be at least one byte.
     */
    public HashValue getHash();

    public int hashCode();

    public boolean equals(Object obj);

    /**
     * The rule for comparison of locator keys is, compare the hash, then
     * compare the key type.
     */
    @Override
    public int compareTo(LocatorKey o);

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
}
