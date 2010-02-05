package org.yi.happy.archive.block;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.archive.BlockParse;
import org.yi.happy.archive.Cipher;
import org.yi.happy.archive.CipherFactory;
import org.yi.happy.archive.DigestProvider;
import org.yi.happy.archive.UnknownDigestException;
import org.yi.happy.archive.key.ContentFullKey;
import org.yi.happy.archive.key.ContentLocatorKey;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.HexEncode;
import org.yi.happy.archive.key.UnknownAlgorithmException;

public final class ContentEncodedBlock extends AbstractBlock implements
	EncodedBlock {
    private final ContentLocatorKey key;
    private final DigestProvider digest;
    private final String cipher;
    private final byte[] body;

    public ContentEncodedBlock(ContentLocatorKey key, DigestProvider digest,
	    String cipher, byte[] body) {

	GenericBlock.checkValue(digest.getAlgorithm());
	GenericBlock.checkValue(cipher);

	byte[] hash = getHash(digest, body);
	if (!Arrays.equals(key.getHash(), hash)) {
	    throw new IllegalArgumentException();
	}

	this.key = key;
	this.digest = digest;
	this.cipher = cipher;
	this.body = body.clone();
    }

    public ContentEncodedBlock(DigestProvider digest, String cipher, byte[] body) {

	GenericBlock.checkValue(digest.getAlgorithm());
	GenericBlock.checkValue(cipher);

	byte[] hash = getHash(digest, body);

	this.key = new ContentLocatorKey(hash);
	this.digest = digest;
	this.cipher = cipher;
	this.body = body.clone();
    }

    public ContentLocatorKey getKey() {
	return key;
    }

    public String getDigest() {
	return digest.getAlgorithm();
    }

    public String getCipher() {
	return cipher;
    }

    public byte[] getBody() {
	return body.clone();
    }

    @Override
    public Map<String, String> getMeta() {
	Map<String,String> out = new LinkedHashMap<String, String>();
	out.put("version", "2");
	out.put("key-type", key.getType());
	out.put("key", HexEncode.encode(key.getHash()));
	out.put("digest", digest.getAlgorithm());
	out.put("cipher", cipher);
	out.put("size", Integer.toString(body.length));
	return out;
    }

    /**
     * get the content hash for a block.
     * 
     * @param digest
     *            the normalized digest name.
     * @param cipher
     *            the normalized cipher name.
     * @param body
     *            the body.
     * @return the hash value.
     */
    public static byte[] getHash(DigestProvider digest, byte[] body) {
	try {
	    MessageDigest d = digest.get();
	    d.update(body);
	    return d.digest();
	} catch (UnknownAlgorithmException e) {
	    throw new UnknownDigestException(digest.getAlgorithm(), e);
	}
    }

    public Block decode(FullKey fullKey) {
	if (!(fullKey instanceof ContentFullKey)) {
	    throw new IllegalArgumentException();
	}
	ContentFullKey k = (ContentFullKey) fullKey;

	Cipher c = CipherFactory.create(this.cipher);
	c.setKey(k.getPass());

	if (body.length % c.getBlockSize() != 0) {
	    throw new IllegalArgumentException(
		    "size is not a multiple of the cipher block size");
	}

	byte[] out = body.clone();
	c.decrypt(out);

	return BlockParse.load(out);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.hashCode(body);
	result = prime * result + ((cipher == null) ? 0 : cipher.hashCode());
	result = prime * result + ((digest == null) ? 0 : digest.hashCode());
	result = prime * result + ((key == null) ? 0 : key.hashCode());
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
	ContentEncodedBlock other = (ContentEncodedBlock) obj;
	if (!Arrays.equals(body, other.body))
	    return false;
	if (cipher == null) {
	    if (other.cipher != null)
		return false;
	} else if (!cipher.equals(other.cipher))
	    return false;
	if (digest == null) {
	    if (other.digest != null)
		return false;
	} else if (!digest.equals(other.digest))
	    return false;
	if (key == null) {
	    if (other.key != null)
		return false;
	} else if (!key.equals(other.key))
	    return false;
	return true;
    }
}
