package org.yi.happy.archive;

import org.yi.happy.crypt.Rijndael;

/**
 * specific implementation for Rijndael cipher.
 */
public class CipherRijndael implements Cipher {
    private Rijndael instance;

    private byte[] iv;

    private final int bs;

    private final int ks;

    private final String algorithm;

    /**
     * create with the given block size and key size
     * 
     * @param bs
     *            the block size
     * @param ks
     *            the key size
     */
    public CipherRijndael(String algorithm, int bs, int ks) {
	this.algorithm = algorithm;
	this.bs = bs;
	this.ks = ks;
	instance = new Rijndael(bs, ks);
    }

    public void decrypt(byte[] out) {
	instance.decryptCbc(out, iv);
    }

    public int getBlockSize() {
	return bs;
    }

    public int getKeySize() {
	return ks;
    }

    public void setKey(byte[] pass) {
	instance.setKey(pass);
	iv = new byte[bs];
    }

    public void encrypt(byte[] data) {
	instance.encryptCbc(data, iv);
    }

    @Override
    public String getAlgorithm() {
	return algorithm;
    }
}
