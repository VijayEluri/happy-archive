package org.yi.happy.archive.key;

/**
 * a full key. the data parts vary by type.
 */
public interface FullKey extends Key {
    public boolean equals(Object obj);

    public int hashCode();

    public String toString();
}
