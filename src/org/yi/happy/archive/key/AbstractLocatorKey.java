package org.yi.happy.archive.key;


/**
 * common function of locator keys
 */
public abstract class AbstractLocatorKey implements LocatorKey {

    private final HashValue hash;

    /**
     * create
     * 
     * @param hash
     *            the hash
     */
    public AbstractLocatorKey(HashValue hash) {
        if (hash == null) {
            throw new IllegalArgumentException("hash is null");
        }
        this.hash = hash;
    }

    /**
     * 
     * @return the hash
     */
    public HashValue getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return getType() + ":" + hash;
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

    @Override
    public int compareTo(LocatorKey o) {
        int out = getHash().compareTo(o.getHash());
        if (out != 0) {
            return out;
        }
        return getType().compareTo(o.getType());
    }
}