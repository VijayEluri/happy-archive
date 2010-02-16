package org.yi.happy.archive.block.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.Sets;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.ContentEncodedBlock;
import org.yi.happy.archive.crypto.CipherFactory;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.key.ContentLocatorKey;
import org.yi.happy.archive.key.KeyParse;

/**
 * parser for a content encoded block.
 */
public class ContentEncodedBlockParse {

    /**
     * parse a content encoded block.
     * 
     * @param block
     *            the block to parse.
     * @return the parsed block.
     * @throws IllegalArgumentException
     *             if the parsing can not be completed.
     */
    @MagicLiteral
    public static ContentEncodedBlock parse(Block block) {
	Map<String, String> meta = block.getMeta();

	if (!meta.containsKey("key-type")) {
	    throw new MissingMetaException();
	}

	if (!meta.get("key-type").equals("content-hash")) {
	    throw new IllegalArgumentException("wrong key-type");
	}

	String version = meta.get("version");
	if (version == null) {
	    return parseVersion1(meta, block);
	}
	if (version.equals("2")) {
	    return parseVersion2(meta, block);
	}
	if (version.equals("1")) {
	    return parseVersion1(meta, block);
	}

	throw new IllegalArgumentException("unknown version");
    }

    @MagicLiteral
    private static ContentEncodedBlock parseVersion2(Map<String, String> meta,
	    Block block) {
	if (!meta.keySet().equals(
		Sets.asSet("version", "key-type", "key", "digest", "cipher",
			"size"))) {
	    throw new IllegalArgumentException("meta missmatch");
	}

	ContentLocatorKey key = KeyParse
		.parseContentLocatorKey(meta.get("key"));
	DigestProvider digest = DigestFactory.getProvider(meta.get("digest"));
	CipherProvider cipher = CipherFactory.getProvider(meta.get("cipher"));
	int size = Integer.parseInt(meta.get("size"));
	Bytes body = block.getBody();

	if (size != body.getSize()) {
	    throw new IllegalArgumentException("size missmatch");
	}

	return new ContentEncodedBlock(key, digest, cipher, body);
    }

    @MagicLiteral
    private static ContentEncodedBlock parseVersion1(Map<String, String> meta,
	    Block block) {
	if (!meta.keySet().containsAll(
		Sets.asSet("key-type", "key", "digest", "cipher", "size"))) {
	    throw new IllegalArgumentException("missing meta-data");
	}
	if (meta.size() == 6 && !meta.containsKey("version")) {
	    throw new IllegalArgumentException("extra meta-data");
	}
	if (meta.size() > 6) {
	    throw new IllegalArgumentException("extra meta-data");
	}

	ContentLocatorKey key = KeyParse
		.parseContentLocatorKey(meta.get("key"));
	DigestProvider digest = DigestFactory.getProvider(meta.get("digest"));
	CipherProvider cipher = CipherFactory.getProvider(fixCipher(meta
		.get("cipher")));
	int size = Integer.parseInt(meta.get("size"));
	Bytes body = block.getBody();

	if (size != body.getSize()) {
	    throw new IllegalArgumentException("size missmatch");
	}

	return new ContentEncodedBlock(key, digest, cipher, body);
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
