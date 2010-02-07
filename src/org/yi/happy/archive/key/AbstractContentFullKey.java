package org.yi.happy.archive.key;

import java.util.Arrays;

public abstract class AbstractContentFullKey {

    public abstract String getType();

    private final byte[] hash;
    private final byte[] pass;

    public AbstractContentFullKey(byte[] hash, byte[] pass) {
	if (hash.length < 1) {
	    throw new IllegalArgumentException("hash too short");
	}

	this.hash = hash.clone();
	this.pass = pass.clone();
    }

    /**
     * 
     * @return the hash
     */
    public byte[] getHash() {
	return hash.clone();
    }

    /**
     * 
     * @return the cipher key
     */
    public byte[] getPass() {
	return pass.clone();
    }

    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.hashCode(hash);
	result = prime * result + Arrays.hashCode(pass);
	return result;
    }

    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	final AbstractContentFullKey other = (AbstractContentFullKey) obj;
	if (!Arrays.equals(hash, other.hash))
	    return false;
	if (!Arrays.equals(pass, other.pass))
	    return false;
	return true;
    }

    public String toString() {
	return getType() + ":" + Base16.encode(hash) + ":"
		+ Base16.encode(pass);
    }

}