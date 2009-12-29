package org.yi.happy.archive;

import java.security.MessageDigest;

/**
 * encode blob blocks
 */
public class BlockEncoderImpl implements BlockEncoder {
    /**
     * the default cipher that is used if one is not set
     */
    public static final String DEFAULT_CIPHER = "aes-128-cbc";

    /**
     * the default digest that is used if one is not set
     */
    public static final String DEFAULT_DIGEST = "sha-256";

    /**
     * the digest to use
     */
    private MessageDigest digest;

    /**
     * the cipher to use
     */
    private NamedCipher cipher;

    /**
     * create with defaults
     */
    public BlockEncoderImpl() {
        digest = null;
        cipher = null;
    }

    /**
     * set the digest to be used
     * 
     * @param digest
     *            the digest to be used
     */
    public void setDigestName(String digest) {
        this.digest = DigestFactory.create(digest);
    }

    /**
     * set the cipher to be used
     * 
     * @param cipher
     *            the cipher to be used
     */
    public void setCipherName(String cipher) {
        this.cipher = CipherFactory.createNamed(cipher);
    }

    /**
     * lazy create the cipher if it was not set
     * 
     * @return the cipher to use
     */
    private NamedCipher getCipher() {
        if (cipher == null) {
            cipher = CipherFactory.createNamed(DEFAULT_CIPHER);
        }
        return cipher;
    }

    /**
     * lazy create the digest if it was not set
     * 
     * @return the digest to use
     */
    private MessageDigest getDigest() {
        if (digest == null) {
            digest = DigestFactory.create(DEFAULT_DIGEST);
        }
        return digest;
    }

    /**
     * get the name of the digest in use
     * 
     * @return the digest algorithm
     */
    public String getDigestName() {
        return getDigest().getAlgorithm();
    }

    /**
     * get the name of the cipher in use
     * 
     * @return the cipher algorithm
     */
    public String getCipherName() {
        return getCipher().getAlgorithm();
    }

    /**
     * encode a block without a hint, which encodes it as a content-hash block.
     * 
     * @param block
     *            the block to encode
     * @return the encoded block
     */
    public BlockEncoderResult encode(Block block) {
        return new BlockEncoderBlob(getDigest(), getCipher()).encode(block);
    }
}
