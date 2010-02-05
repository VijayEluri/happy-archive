package org.yi.happy.archive.block;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.archive.BlockParse;
import org.yi.happy.archive.BlockUtil;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Cipher;
import org.yi.happy.archive.CipherFactory;
import org.yi.happy.archive.DigestFactory;
import org.yi.happy.archive.VerifyException;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.HexEncode;
import org.yi.happy.archive.key.NameFullKey;
import org.yi.happy.archive.key.NameLocatorKey;

public final class NameEncodedBlock extends AbstractBlock implements
	EncodedBlock {

    private final NameLocatorKey key;
    private final byte[] hash;
    private final String digest;
    private final String cipher;
    private final byte[] body;

    public NameEncodedBlock(NameLocatorKey key, byte[] hash, String digest,
	    String cipher, byte[] body) {
	GenericBlock.checkValue(digest);
	GenericBlock.checkValue(cipher);

	byte[] hash0 = ContentEncodedBlock.getHash(digest, body);
	if (!Arrays.equals(hash0, hash)) {
	    throw new VerifyException();
	}

	this.key = key;
	this.hash = hash.clone();
	this.digest = digest;
	this.cipher = cipher;
	this.body = body.clone();
    }

    public NameEncodedBlock(NameLocatorKey key, String digest, String cipher,
	    byte[] body) {
	GenericBlock.checkValue(digest);
	GenericBlock.checkValue(cipher);

	byte[] hash = ContentEncodedBlock.getHash(digest, body);

	this.key = key;
	this.hash = hash.clone();
	this.digest = digest;
	this.cipher = cipher;
	this.body = body.clone();
    }

    public NameLocatorKey getKey() {
	return key;
    }

    public byte[] getHash() {
	return hash.clone();
    }

    public String getDigest() {
	return digest;
    }

    public String getCipher() {
	return cipher;
    }

    public byte[] getBody() {
	return body.clone();
    }

    @Override
    public Map<String, String> getMeta() {
	Map<String, String> out = new LinkedHashMap<String, String>();
	out.put("version", "2");
	out.put("key-type", key.getType());
	out.put("key", HexEncode.encode(key.getHash()));
	out.put("hash", HexEncode.encode(hash));
	out.put("digest", digest);
	out.put("cipher", cipher);
	out.put("size", Integer.toString(body.length));
	return out;
    }

    public Block decode(FullKey fullKey) {
	if (!(fullKey instanceof NameFullKey)) {
	    throw new IllegalArgumentException();
	}
	NameFullKey k = (NameFullKey) fullKey;

	/*
	 * get the cipher
	 */
	Cipher c = CipherFactory.create(this.cipher);

	/*
	 * get the key from the name
	 */
	String algo = k.getDigest();
	byte[] part = ByteString.toUtf8(k.getName());
	MessageDigest md = DigestFactory.create(algo);

	c.setKey(BlockUtil.expandKey(md, part, c.getKeySize()));

	/*
	 * decrypt the body
	 */
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
	result = prime * result + Arrays.hashCode(hash);
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
	NameEncodedBlock other = (NameEncodedBlock) obj;
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
	if (!Arrays.equals(hash, other.hash))
	    return false;
	if (key == null) {
	    if (other.key != null)
		return false;
	} else if (!key.equals(other.key))
	    return false;
	return true;
    }
}
