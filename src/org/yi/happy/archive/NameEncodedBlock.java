package org.yi.happy.archive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;

import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.HexEncode;
import org.yi.happy.archive.key.NameFullKey;
import org.yi.happy.archive.key.NameLocatorKey;

public final class NameEncodedBlock implements EncodedBlock {

    private final NameLocatorKey key;
    private final byte[] hash;
    private final String digest;
    private final String cipher;
    private final byte[] body;

    public NameEncodedBlock(NameLocatorKey key, byte[] hash, String digest,
	    String cipher, byte[] body) {
	BlockImpl.checkValue(digest);
	BlockImpl.checkValue(cipher);

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
	BlockImpl.checkValue(digest);
	BlockImpl.checkValue(cipher);

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

    public int getSize() {
	return body.length;
    }

    public byte[] getBody() {
	return body.clone();
    }

    public String getMeta(String name) {
	if (name.equals("version")) {
	    return "2";
	}

	if (name.equals("key-type")) {
	    return key.getType();
	}

	if (name.equals("key")) {
	    return HexEncode.encode(key.getHash());
	}

	if (name.equals("hash")) {
	    return HexEncode.encode(hash);
	}

	if (name.equals("digest")) {
	    return digest;
	}

	if (name.equals("cipher")) {
	    return cipher;
	}

	if (name.equals("size")) {
	    return Integer.toString(body.length);
	}

	return null;
    }

    public byte[] asBytes() {
	try {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();

	    out.write(ByteString.toUtf8("version: 2\r\nkey-type: "));
	    out.write(ByteString.toUtf8(key.getType()));
	    out.write(ByteString.toUtf8("\r\nkey: "));
	    out.write(ByteString.toUtf8(HexEncode.encode(key.getHash())));
	    out.write(ByteString.toUtf8("\r\nhash: "));
	    out.write(ByteString.toUtf8(HexEncode.encode(hash)));
	    out.write(ByteString.toUtf8("\r\ndigest: "));
	    out.write(ByteString.toUtf8(digest));
	    out.write(ByteString.toUtf8("\r\ncipher: "));
	    out.write(ByteString.toUtf8(cipher));
	    out.write(ByteString.toUtf8("\r\nsize: "));
	    out.write(ByteString.toUtf8(Integer.toString(body.length)));
	    out.write(ByteString.toUtf8("\r\n\r\n"));
	    out.write(body);

	    return out.toByteArray();
	} catch (IOException e) {
	    throw new Error(e);
	}
    }

    public int getRawSize() {
	return asBytes().length;
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
	try {
	    String algo = k.getDigest();
	    byte[] part = k.getName().getBytes("UTF-8");
	    MessageDigest md = DigestFactory.create(algo);

	    c.setPass(BlockUtil.expandKey(md, part, c.getKeySize()));
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException(e);
	}

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
