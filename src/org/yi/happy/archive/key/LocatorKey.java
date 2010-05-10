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
     * @return the string that parses into this locator key
     */
    public String toString();

    public int hashCode();

    public boolean equals(Object obj);
}
