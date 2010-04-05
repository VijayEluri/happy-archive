package org.yi.happy.archive.key;

import org.yi.happy.archive.Base16;
import org.yi.happy.archive.Bytes;

/**
 * common function of locator keys
 */
public abstract class AbstractLocatorKey {

    private final Bytes hash;

    /**
     * create
     * 
     * @param hash
     *            the hash
     */
    public AbstractLocatorKey(Bytes hash) {
        if (hash.getSize() < 1) {
            throw new IllegalArgumentException("hash too short");
        }
        this.hash = hash;
    }

    /**
     * 
     * @return the hash
     */
    public Bytes getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return getType() + ":" + Base16.encode(hash);
    }

    /**
     * @return the type of the locator key
     */
    public abstract String getType();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getType().hashCode();
        result = prime * result + hash.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AbstractLocatorKey other = (AbstractLocatorKey) obj;
        if (!hash.equals(other.hash))
            return false;
        return true;
    }

}