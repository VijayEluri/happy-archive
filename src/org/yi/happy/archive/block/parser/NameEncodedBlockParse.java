package org.yi.happy.archive.block.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.Base16;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.Sets;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.NameEncodedBlock;
import org.yi.happy.archive.crypto.CipherFactory;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.key.NameLocatorKey;

/**
 * parser for {@link NameEncodedBlock}.
 */
public class NameEncodedBlockParse {
    /**
     * parse a {@link Block} into a {@link NameEncodedBlock}.
     * 
     * @param block
     *            the {@link Block} to parse.
     * @return the resulting {@link NameEncodedBlock}.
     */
    @MagicLiteral
    public static NameEncodedBlock parse(Block block) {
	if (block instanceof NameEncodedBlock) {
	    return (NameEncodedBlock) block;
	}

	Map<String, String> meta = block.getMeta();

	String version = meta.get("version");
	if (version == null) {
	    return parseVersion1(meta, block);
	}
	if (version.equals("1")) {
	    return parseVersion1(meta, block);
	}
	if (version.equals("2")) {
	    return parseVersion2(meta, block);
	}

	throw new IllegalArgumentException("unknown version");
    }

    @MagicLiteral
    private static NameEncodedBlock parseVersion2(Map<String, String> meta,
	    Block block) {
	if (!meta.keySet().equals(
		Sets.asSet("version", "key-type", "key", "digest", "cipher",
			"hash", "size"))) {
	    throw new IllegalArgumentException("meta missmatch");
	}

	if (!meta.get("key-type").equals("name-hash")) {
	    throw new IllegalArgumentException("wrong block type");
	}

	NameLocatorKey key = KeyParse.parseNameLocatorKey(meta.get("key"));
	DigestProvider digest = DigestFactory.getProvider(meta.get("digest"));
	CipherProvider cipher = CipherFactory.getProvider(meta.get("cipher"));
	Bytes hash = new Bytes(Base16.decode(meta.get("hash")));
	int size = Integer.parseInt(meta.get("size"));
	Bytes body = block.getBody();

	if (body.getSize() != size) {
	    throw new IllegalArgumentException("size missmatch");
	}

	return new NameEncodedBlock(key, hash, digest, cipher, body);
    }

    @MagicLiteral
    private static NameEncodedBlock parseVersion1(Map<String, String> meta,
	    Block block) {
	if (meta.size() > 6) {
	    throw new IllegalArgumentException("extra meta-data");
	}
	if (!meta.keySet().containsAll(
		Sets.asSet("key-type", "key", "digest", "cipher", "size"))) {
	    throw new IllegalArgumentException("missing meta-data");
	}
	if (meta.size() == 6 && !meta.containsKey("version")) {
	    throw new IllegalArgumentException("extra meta-data");
	}

	if (!meta.get("key-type").equals("name-hash")) {
	    throw new IllegalArgumentException("wrong block type");
	}

	NameLocatorKey key = KeyParse.parseNameLocatorKey(meta.get("key"));
	DigestProvider digest = DigestFactory.getProvider(meta.get("digest"));
	CipherProvider cipher = CipherFactory.getProvider(fixCipher(meta
		.get("cipher")));
	Bytes hash = new Bytes(Base16.decode(meta.get("hash")));
	int size = Integer.parseInt(meta.get("size"));
	Bytes body = block.getBody();

	if (body.getSize() != size) {
	    throw new IllegalArgumentException("size missmatch");
	}

	return new NameEncodedBlock(key, hash, digest, cipher, body);
    }

    private static String fixCipher(String cipher) {
	String name = fixMap.get(cipher);
	if (name == null) {
	    return cipher;
	}
	return name;
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
}
