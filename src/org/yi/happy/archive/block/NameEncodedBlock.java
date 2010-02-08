package org.yi.happy.archive.block;

import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.archive.Base16;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.VerifyException;
import org.yi.happy.archive.block.parser.BlockParse;
import org.yi.happy.archive.crypto.Cipher;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.crypto.Digests;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.NameFullKey;
import org.yi.happy.archive.key.NameLocatorKey;

public final class NameEncodedBlock extends AbstractBlock implements
	EncodedBlock {

    private final NameLocatorKey key;
    private final Bytes hash;
    private final DigestProvider digest;
    private final CipherProvider cipher;
    private final Bytes body;

    public NameEncodedBlock(NameLocatorKey key, Bytes hash,
	    DigestProvider digest, CipherProvider cipher, Bytes body) {
	GenericBlock.checkHeader("digest", digest.getAlgorithm());
	GenericBlock.checkHeader("cipher", cipher.getAlgorithm());

	byte[] hash0 = ContentEncodedBlock.getHash(digest, body);
	if (!hash.equalBytes(hash0)) {
	    throw new VerifyException();
	}

	this.key = key;
	this.hash = hash;
	this.digest = digest;
	this.cipher = cipher;
	this.body = body;
    }

    public NameEncodedBlock(NameLocatorKey key, DigestProvider digest,
	    CipherProvider cipher, Bytes body) {
	GenericBlock.checkHeader("digest", digest.getAlgorithm());
	GenericBlock.checkHeader("cipher", cipher.getAlgorithm());

	byte[] hash = ContentEncodedBlock.getHash(digest, body);

	this.key = key;
	this.hash = new Bytes(hash);
	this.digest = digest;
	this.cipher = cipher;
	this.body = body;
    }

    public NameLocatorKey getKey() {
	return key;
    }

    public Bytes getHash() {
	return hash;
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
	Map<String, String> out = new LinkedHashMap<String, String>();
	out.put("version", "2");
	out.put("key-type", key.getType());
	out.put("key", Base16.encode(key.getHash()));
	out.put("hash", Base16.encode(hash));
	out.put("digest", digest.getAlgorithm());
	out.put("cipher", cipher.getAlgorithm());
	out.put("size", Integer.toString(body.getSize()));
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
	Cipher c = cipher.get();

	/*
	 * get the key from the name
	 */
	DigestProvider algo = k.getDigest();
	byte[] part = ByteString.toUtf8(k.getName());

	c.setKey(Digests.expandKey(algo, part, c.getKeySize()));

	/*
	 * decrypt the body
	 */
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
	result = prime * result + ((cipher == null) ? 0 : cipher.hashCode());
	result = prime * result + ((digest == null) ? 0 : digest.hashCode());
	result = prime * result + hash.hashCode();
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
	if (!hash.equals(other.hash))
	    return false;
	if (key == null) {
	    if (other.key != null)
		return false;
	} else if (!key.equals(other.key))
	    return false;
	return true;
    }
}
