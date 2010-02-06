package org.yi.happy.archive.block.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.yi.happy.annotate.TypeSwitch;
import org.yi.happy.archive.ShortBodyException;
import org.yi.happy.archive.VerifyException;
import org.yi.happy.archive.VersionNotNumberException;
import org.yi.happy.archive.block.BlobEncodedBlock;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.ContentEncodedBlock;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.NameEncodedBlock;
import org.yi.happy.archive.crypto.Cipher;
import org.yi.happy.archive.crypto.CipherFactory;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.key.BlobLocatorKey;
import org.yi.happy.archive.key.ContentLocatorKey;
import org.yi.happy.archive.key.HexDecode;
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.NameLocatorKey;

public class EncodedBlockFactory {
    public static BlobEncodedBlock create(DigestProvider digest,
	    CipherProvider cipher, byte[] body) {
	cipher = normalizeCipherName(cipher);

	return new BlobEncodedBlock(digest, cipher, body);
    }

    public static NameEncodedBlock createName(NameLocatorKey key,
	    DigestProvider digest, CipherProvider cipher, byte[] body) {
	cipher = normalizeCipherName(cipher);

	return new NameEncodedBlock(key, digest, cipher, body);
    }

    private static String normalizeCipherName(String cipher) {
	String c = normalizeMap.get(cipher);
	if (c != null) {
	    return c;
	}

	return cipher;
    }

    private static CipherProvider normalizeCipherName(
	    final CipherProvider cipher) {
	String c = normalizeCipherName(cipher.getAlgorithm());
	if (c.equals(cipher.getAlgorithm())) {
	    return cipher;
	}
	return new CipherProvider(c) {
	    @Override
	    public Cipher get() {
		return cipher.get();
	    }
	};
    }

    @TypeSwitch
    public static EncodedBlock parse(Block block) throws VerifyException {
	if (block instanceof EncodedBlock) {
	    return (EncodedBlock) block;
	}

	String keyType = getKeyType(block);
	byte[] keyHash = getKeyHash(block);

	LocatorKey key = KeyParse.parseLocatorKey(keyType, keyHash);

	if (key instanceof BlobLocatorKey) {
	    return parseBlob(block, (BlobLocatorKey) key);
	}

	if (key instanceof ContentLocatorKey) {
	    return parseContent(block, (ContentLocatorKey) key);
	}

	if (key instanceof NameLocatorKey) {
	    return parseName(block, (NameLocatorKey) key);
	}

	throw new UnknownKeyTypeException();
    }

    private static EncodedBlock parseName(Block block, NameLocatorKey key)
	    throws VerifyException {
	byte[] body = getBody(block);

	String digest = getDigestName(block);

	String cipher = getCipherName(block);

	int version = getVersion(block);

	if (version < 2) {
	    cipher = fixCipher(cipher);
	}

	byte[] hash = getHash(block);

	return new NameEncodedBlock(key, hash, DigestFactory
		.getProvider(digest), CipherFactory.getProvider(cipher), body);
    }

    private static byte[] getHash(Block block) throws VerifyException {
	try {
	    return HexDecode.decode(getRequiredMeta(block, "hash"));
	} catch (VerifyException e) {
	    throw e;
	} catch (IllegalArgumentException e) {
	    throw new VerifyException();
	}

    }

    private static EncodedBlock parseContent(Block block, ContentLocatorKey key)
	    throws VerifyException {
	byte[] body = getBody(block);

	String digest = getDigestName(block);

	String cipher = getCipherName(block);

	int version = getVersion(block);

	if (version < 2) {
	    cipher = fixCipher(cipher);
	}

	return new ContentEncodedBlock(key, DigestFactory.getProvider(digest),
		CipherFactory.getProvider(cipher), body);
    }

    private static String fixCipher(String cipher) {
	String c = fixMap.get(cipher);
	if (c != null) {
	    return c;
	}

	return cipher;
    }

    private static int getVersion(Block block) throws VerifyException {
	String version = block.getMeta().get("version");

	if (version == null) {
	    return 1;
	}

	try {
	    return Integer.parseInt(version);
	} catch (NumberFormatException e) {
	    throw new VersionNotNumberException(e);
	}
    }

    private static byte[] getKeyHash(Block block) throws VerifyException {
	try {
	    return HexDecode.decode(getRequiredMeta(block, "key"));
	} catch (VerifyException e) {
	    throw e;
	} catch (IllegalArgumentException e) {
	    throw new VerifyException(e);
	}
    }

    private static EncodedBlock parseBlob(Block block, BlobLocatorKey key)
	    throws VerifyException {
	byte[] body = getBody(block);

	String digest = getDigestName(block);

	String cipher = getCipherName(block);

	return new BlobEncodedBlock(key, DigestFactory.getProvider(digest),
		CipherFactory.getProvider(cipher), body);
    }

    private static byte[] getBody(Block block) throws VerifyException {
	int size = getSize(block);

	if (size < 0) {
	    throw new NegativeSizeException();
	}

	byte[] body = block.getBody();

	if (body.length < size) {
	    throw new ShortBodyException();
	}

	if (body.length > size) {
	    byte[] tmp = new byte[size];
	    System.arraycopy(body, 0, tmp, 0, size);
	    body = tmp;
	}

	return body;
    }

    private static String getCipherName(Block block) throws VerifyException {
	return getRequiredMeta(block, "cipher");
    }

    private static String getDigestName(Block block) throws VerifyException {
	return getRequiredMeta(block, "digest");
    }

    private static int getSize(Block block) throws VerifyException {
	String size = getRequiredMeta(block, "size");
	return Integer.parseInt(size);
    }

    private static String getKeyType(Block block) throws VerifyException {
	return getRequiredMeta(block, "key-type");
    }

    private static String getRequiredMeta(Block block, String name)
	    throws VerifyException {
	String value = block.getMeta().get(name);
	if (value == null) {
	    throw new MissingMetaException();
	}
	return value;
    }

    public static ContentEncodedBlock createContent(DigestProvider digest,
	    CipherProvider cipher, byte[] body) {
	cipher = normalizeCipherName(cipher);

	return new ContentEncodedBlock(digest, cipher, body);
    }

    /**
     * translation map to fix cipher names between version 1 and 2.
     */
    public static final Map<String, String> fixMap;

    static {
	Map<String, String> m = new HashMap<String, String>(2);
	m.put("aes-192-cbc", "rijndael192-192-cbc");
	m.put("aes-256-cbc", "rijndael256-256-cbc");
	fixMap = Collections.unmodifiableMap(m);
    }

    /**
     * translation map to normalize cipher names.
     */
    public static final Map<String, String> normalizeMap;

    static {
	Map<String, String> n = new HashMap<String, String>(3);
	n.put("rijndael-128-cbc", "aes-128-cbc");
	n.put("rijndael-192-cbc", "aes-192-cbc");
	n.put("rijndael-256-cbc", "aes-256-cbc");
	normalizeMap = Collections.unmodifiableMap(n);
    }
}
