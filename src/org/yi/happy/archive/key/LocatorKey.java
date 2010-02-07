package org.yi.happy.archive.key;

import org.yi.happy.archive.Bytes;

/**
 * a locator key, a reference to a raw block. there are no more data parts than
 * just the type and hash.
 */
public interface LocatorKey extends Key {
    /**
     * 
     * @return the hash. must be at least one byte.
     */
    public Bytes getHash();

    public int hashCode();

    public boolean equals(Object obj);

    public String toString();
}
