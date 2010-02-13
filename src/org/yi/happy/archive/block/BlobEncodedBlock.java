package org.yi.happy.archive.block;

import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.archive.BadSignatureException;
import org.yi.happy.archive.Base16;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.UnknownDigestAlgorithmException;
import org.yi.happy.archive.block.parser.BlockParse;
import org.yi.happy.archive.crypto.Cipher;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.key.BlobFullKey;
import org.yi.happy.archive.key.BlobLocatorKey;
import org.yi.happy.archive.key.FullKey;

/**
 * A valid blob encoded block.
 */
public final class BlobEncodedBlock extends AbstractBlock implements
	EncodedBlock {
    private final BlobLocatorKey key;
    private final DigestProvider digest;
    private final CipherProvider cipher;
    private final Bytes body;

    /**
     * create with all details available, they are checked.
     * 
     * @param key
     *            the locator key for the block.
     * @param digest
     *            the digest being used.
     * @param cipher
     *            the cipher being used.
     * @param body
     *            the bytes of the body.
     * @throws IllegalArgumentException
     *             if the details do not check out.
     * @throws BadSignatureException
     *             if the signature of the body does not match the signature
     *             given in the key.
     * @throws UnknownDigestAlgorithmException
     *             if the digest provider can not create the digest
     *             implementation.
     */
    public BlobEncodedBlock(BlobLocatorKey key, DigestProvider digest,
	    CipherProvider cipher, Bytes body) throws IllegalArgumentException,
	    BadSignatureException, UnknownDigestAlgorithmException {
	GenericBlock.checkHeader("digest", digest.getAlgorithm());
	GenericBlock.checkHeader("cipher", cipher.getAlgorithm());

	byte[] hash = getHash(digest, cipher, body);
	if (!key.getHash().equalBytes(hash)) {
	    throw new BadSignatureException();
	}

	this.key = key;
	this.digest = digest;
	this.cipher = cipher;
	this.body = body;
    }

    /**
     * create with minimal details, the rest are calculated.
     * 
     * @param digest
     *            the digest being used.
     * @param cipher
     *            the cipher being used.
     * @param body
     *            the bytes of the body.
     * @throws IllegalArgumentException
     *             if the details are invalid.
     */
    public BlobEncodedBlock(DigestProvider digest, CipherProvider cipher,
	    Bytes body) {
	GenericBlock.checkHeader("digest", digest.getAlgorithm());
	GenericBlock.checkHeader("cipher", cipher.getAlgorithm());

	byte[] hash = getHash(digest, cipher, body);

	this.key = new BlobLocatorKey(new Bytes(hash));
	this.digest = digest;
	this.cipher = cipher;
	this.body = body;
    }

    public BlobLocatorKey getKey() {
	return key;
    }

    public DigestProvider getDigest() {
	return digest;
    }

    public CipherProvider getCipher() {
	return cipher;
    }

    @Override
    public Bytes getBody() {
	return body;
    }

    @Override
    public Map<String, String> getMeta() {
	Map<String, String> out = new LinkedHashMap<String, String>();
	out.put("key-type", key.getType());
	out.put("key", Base16.encode(key.getHash()));
	out.put("digest", digest.getAlgorithm());
	out.put("cipher", cipher.getAlgorithm());
	out.put("size", Integer.toString(body.getSize()));
	return out;
    }

    /**
     * get the blob hash for a block.
     * 
     * @param digest
     *            the normalized digest name.
     * @param cipher
     *            the normalized cipher name.
     * @param body
     *            the body.
     * @return the hash value.
     */
    public byte[] getHash(DigestProvider digest, CipherProvider cipher,
	    Bytes body) {
	MessageDigest d = digest.get();
	d.update(ByteString.toUtf8("digest: "));
	d.update(ByteString.toUtf8(digest.getAlgorithm()));
	d.update(ByteString.toUtf8("\r\ncipher: "));
	d.update(ByteString.toUtf8(cipher.getAlgorithm()));
	d.update(ByteString.toUtf8("\r\nsize: "));
	d.update(ByteString.toUtf8(Integer.toString(body.getSize())));
	d.update(ByteString.toUtf8("\r\n\r\n"));
	d.update(body.toByteArray());
	return d.digest();
    }

    public Block decode(FullKey fullKey) {
	if (!fullKey.toLocatorKey().equals(key)) {
	    throw new IllegalArgumentException("the key is not for this block");
	}
	BlobFullKey k = (BlobFullKey) fullKey;

	Cipher c = cipher.get();
	c.setKey(k.getPass().toByteArray());

	if (body.getSize() % c.getBlockSize() != 0) {
	    throw new IllegalArgumentException(
		    "body size is not a multiple of the cipher block size");
	}

	byte[] out = body.toByteArray();
	c.decrypt(out);

	return BlockParse.parse(out);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + body.hashCode();
	result = prime * result + cipher.hashCode();
	result = prime * result + digest.hashCode();
	result = prime * result + key.hashCode();
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
	BlobEncodedBlock other = (BlobEncodedBlock) obj;
	if (!body.equals(other.body))
	    return false;
	if (!cipher.equals(other.cipher))
	    return false;
	if (!digest.equals(other.digest))
	    return false;
	if (!key.equals(other.key))
	    return false;
	return true;
    }
}
