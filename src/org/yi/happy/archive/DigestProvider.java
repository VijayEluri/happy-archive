package org.yi.happy.archive;

import java.security.MessageDigest;

import org.yi.happy.archive.key.UnknownAlgorithmException;

public abstract class DigestProvider {
    protected final String algorithm;

    public DigestProvider(String algorithm) {
	this.algorithm = algorithm;
    }

    /**
     * Provide an instance of the digest.
     * 
     * @return an instance of the digest.
     * @throws UnknownAlgorithmException
     *             if the algorithm does not have a known implementation.
     */
    public abstract MessageDigest get() throws UnknownAlgorithmException;

    /**
     * @return the digest algorithm that will be provided.
     */
    public final String getAlgorithm() {
	return algorithm;
    }

    @Override
    public final int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((algorithm == null) ? 0 : algorithm.hashCode());
	return result;
    }

    @Override
    public final boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	DigestProvider other = (DigestProvider) obj;
	if (algorithm == null) {
	    if (other.algorithm != null)
		return false;
	} else if (!algorithm.equals(other.algorithm))
	    return false;
	return true;
    }

    @Override
    public final String toString() {
	return algorithm;
    }
}
