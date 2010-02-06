package org.yi.happy.archive.key;

import org.yi.happy.archive.DigestProvider;

/**
 * a name full key
 */
public final class NameFullKey implements FullKey {

    public String getType() {
	return KeyType.NAME_HASH;
    }

    private final DigestProvider digest;

    private final String name;

    /**
     * @param digest
     * @param name
     */
    public NameFullKey(DigestProvider digest, String name) {
	if (digest == null) {
	    throw new NullPointerException("digest");
	}

	if (digest.getAlgorithm().contains(":")
		|| digest.getAlgorithm().length() < 1) {
	    throw new IllegalArgumentException("digest not valid");
	}

	if (name == null) {
	    throw new NullPointerException("name");
	}

	this.digest = digest;
	this.name = name;
    }

    /**
     * @return the digest to use
     */
    public DigestProvider getDigest() {
	return digest;
    }

    /**
     * 
     * @return the name to use
     */
    public String getName() {
	return name;
    }

    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + digest.hashCode();
	result = prime * result + name.hashCode();
	return result;
    }

    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	final NameFullKey other = (NameFullKey) obj;
	if (!digest.equals(other.digest))
	    return false;
	if (!name.equals(other.name))
	    return false;
	return true;
    }

    public String toString() {
	return getType() + ":" + getDigest() + ":" + getName();
    }
}
