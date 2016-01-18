package org.yi.happy.archive.crypto;

import org.yi.happy.crypt.Rijndael;

/**
 * specific implementation for Rijndael cipher.
 */
public class CipherRijndael implements Cipher {
    private Rijndael instance;

    private byte[] iv;

    private final int bs;

    private final int ks;

    /**
     * create with the given block size and key size
     * 
     * @param bs
     *            the block size
     * @param ks
     *            the key size
     */
    public CipherRijndael(int bs, int ks) {
        this.bs = bs;
        this.ks = ks;
        instance = new Rijndael(bs, ks);
    }

    @Override
    public void decrypt(byte[] out) {
        instance.decryptCbc(out, iv);
    }

    @Override
    public int getBlockSize() {
        return bs;
    }

    @Override
    public int getKeySize() {
        return ks;
    }

    @Override
    public void setKey(byte[] pass) {
        instance.setKey(pass);
        iv = new byte[bs];
    }

    @Override
    public void encrypt(byte[] data) {
        instance.encryptCbc(data, iv);
    }
}
