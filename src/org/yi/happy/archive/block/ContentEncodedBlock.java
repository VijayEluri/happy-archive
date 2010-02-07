package org.yi.happy.archive.block;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.UnknownDigestException;
import org.yi.happy.archive.block.parser.BlockParse;
import org.yi.happy.archive.crypto.Cipher;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.crypto.Digests;
import org.yi.happy.archive.crypto.UnknownAlgorithmException;
import org.yi.happy.archive.key.Base16;
import org.yi.happy.archive.key.ContentFullKey;
import org.yi.happy.archive.key.ContentLocatorKey;
import org.yi.happy.archive.key.FullKey;

public final class ContentEncodedBlock extends AbstractBlock implements
	EncodedBlock {
    private final ContentLocatorKey key;
    private final DigestProvider digest;
    private final CipherProvider cipher;
    private final Bytes body;

    public ContentEncodedBlock(ContentLocatorKey key, DigestProvider digest,
	    CipherProvider cipher, Bytes body) {

	GenericBlock.checkValue(digest.getAlgorithm());
	GenericBlock.checkValue(cipher.getAlgorithm());

	byte[] hash = getHash(digest, body);
	if (!Arrays.equals(key.getHash(), hash)) {
	    throw new IllegalArgumentException();
	}

	this.key = key;
	this.digest = digest;
	this.cipher = cipher;
	this.body = body;
    }

    public ContentEncodedBlock(DigestProvider digest, CipherProvider cipher,
	    Bytes body) {

	GenericBlock.checkValue(digest.getAlgorithm());
	GenericBlock.checkValue(cipher.getAlgorithm());

	byte[] hash = getHash(digest, body);

	this.key = new ContentLocatorKey(hash);
	this.digest = digest;
	this.cipher = cipher;
	this.body = body;
    }

    public ContentLocatorKey getKey() {
	return key;
    }

    public DigestProvider getDigest() {
	return digest;
    }

    public CipherProvider getCipher() {
	return cipher;
    }

    public Bytes getBody() {
	return body;
    }

    @Override
    public Map<String, String> getMeta() {
	Map<String,String> out = new LinkedHashMap<String, String>();
	out.put("version", "2");
	out.put("key-type", key.getType());
	out.put("key", Base16.encode(key.getHash()));
	out.put("digest", digest.getAlgorithm());
	out.put("cipher", cipher.getAlgorithm());
	out.put("size", Integer.toString(body.getSize()));
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
    public static byte[] getHash(DigestProvider digest, Bytes body) {
	try {
	    return Digests.digestData(digest, body);
	} catch (UnknownAlgorithmException e) {
	    throw new UnknownDigestException(digest.getAlgorithm(), e);
	}
    }

    public Block decode(FullKey fullKey) {
	if (!(fullKey instanceof ContentFullKey)) {
	    throw new IllegalArgumentException();
	}
	ContentFullKey k = (ContentFullKey) fullKey;

	Cipher c = cipher.get();
	c.setKey(k.getPass());

	if (body.getSize() % c.getBlockSize() != 0) {
	    throw new IllegalArgumentException(
		    "size is not a multiple of the cipher block size");
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
	ContentEncodedBlock other = (ContentEncodedBlock) obj;
	if (!body.equals(other.body))
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
