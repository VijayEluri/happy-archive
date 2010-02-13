package org.yi.happy.archive.key;

import org.yi.happy.archive.Base16;
import org.yi.happy.archive.Bytes;

public abstract class AbstractContentFullKey {

    public abstract String getType();

    private final Bytes hash;
    private final Bytes pass;

    public AbstractContentFullKey(Bytes hash, Bytes pass) {
	if (hash.getSize() < 1) {
	    throw new IllegalArgumentException("hash too short");
	}

	if (pass == null) {
	    throw new IllegalArgumentException("pass is null");
	}

	this.hash = hash;
	this.pass = pass;
    }

    /**
     * 
     * @return the hash
     */
    public Bytes getHash() {
	return hash;
    }

    /**
     * 
     * @return the cipher key
     */
    public Bytes getPass() {
	return pass;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + hash.hashCode();
	result = prime * result + pass.hashCode();
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
	final AbstractContentFullKey other = (AbstractContentFullKey) obj;
	if (!hash.equals(other.hash))
	    return false;
	if (!pass.equals(other.pass))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return getType() + ":" + Base16.encode(hash) + ":"
		+ Base16.encode(pass);
    }

}