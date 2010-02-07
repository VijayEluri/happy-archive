package org.yi.happy.archive.key;

import java.util.Arrays;

/**
 * common function of locator keys
 */
public abstract class AbstractLocatorKey {

    private final byte[] hash;

    /**
     * create
     * 
     * @param hash
     *            the hash
     */
    public AbstractLocatorKey(byte[] hash) {
	if (hash.length < 1) {
	    throw new IllegalArgumentException("hash too short");
	}
	this.hash = hash.clone();
    }

    /**
     * 
     * @return the hash
     */
    public byte[] getHash() {
	return hash.clone();
    }

    public String toString() {
	return getType() + ":" + Base16.encode(hash);
    }

    /**
     * @return the type of the locator key
     */
    public abstract String getType();

    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + getType().hashCode();
	result = prime * result + Arrays.hashCode(hash);
	return result;
    }

    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	final AbstractLocatorKey other = (AbstractLocatorKey) obj;
	if (!Arrays.equals(hash, other.hash))
	    return false;
	return true;
    }

}