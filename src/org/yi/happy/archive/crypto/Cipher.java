package org.yi.happy.archive.crypto;

/**
 * interface for using various ciphers.
 */
public interface Cipher {
    /**
     * set the encryption/decryption key
     * 
     * @param pass
     */
    void setKey(byte[] pass);

    /**
     * get the block size
     * 
     * @return the block size
     */
    int getBlockSize();

    /**
     * decrypt a set of blocks
     * 
     * @param data
     *            the block of data to decrypt in place
     */
    void decrypt(byte[] data);

    /**
     * get the key size
     * 
     * @return the key size
     */
    int getKeySize();

    /**
     * encrypt a set of blocks in place.
     * 
     * @param data
     *            the blocks to encrypt
     */
    void encrypt(byte[] data);
}
