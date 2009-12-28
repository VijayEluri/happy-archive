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

    public int hashCode();

    public boolean equals(Object obj);
}
