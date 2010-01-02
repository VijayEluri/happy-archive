package org.yi.happy.archive;

/**
 * a cipher that also knows it's name. I was created so that the Cipher
 * interface can more closely match the MessageDigest interface without
 * polluting the specific cipher implementations.
 */
public class NamedCipher implements Cipher {
    private final Cipher delegate;

    private final String algorithm;

    /**
     * create delegating to the given cipher
     * 
     * @param algorithm
     *            the algorithm used to create delegate
     * @param delegate
     *            an object implementing the cipher interface
     */
    public NamedCipher(String algorithm, Cipher delegate) {
	this.algorithm = algorithm;
	this.delegate = delegate;
    }

    public void decrypt(byte[] data) {
	delegate.decrypt(data);
    }

    public void encrypt(byte[] data) {
	delegate.encrypt(data);
    }

    public int getBlockSize() {
	return delegate.getBlockSize();
    }

    public int getKeySize() {
	return delegate.getKeySize();
    }

    public void setPass(byte[] pass) {
	delegate.setPass(pass);
    }

    /**
     * get the algorithm used for creation of this cipher
     * 
     * @return the algorithm
     */
    public String getAlgorithm() {
	return algorithm;
    }
}
