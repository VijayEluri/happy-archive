package org.yi.happy.archive.block.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.yi.happy.annotate.ExternalName;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.ContentEncodedBlock;
import org.yi.happy.archive.crypto.CipherFactory;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.key.ContentLocatorKey;
import org.yi.happy.archive.key.KeyType;
import org.yi.happy.archive.key.LocatorKeyParse;

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
    public static ContentEncodedBlock parse(Block block) {
        if (block instanceof ContentEncodedBlock) {
            return (ContentEncodedBlock) block;
        }

        Map<String, String> meta = block.getMeta();

        if (!meta.containsKey(ContentEncodedBlock.VERSION_META)) {
            return parseVersion1(meta, block);
        }
        return parseVersion2(meta, block);
    }

    /**
     * The current version meta-data field set.
     */
    private static final Set<String> META = new SetBuilder<String>(
            ContentEncodedBlock.VERSION_META,
            ContentEncodedBlock.KEY_TYPE_META, ContentEncodedBlock.KEY_META,
            ContentEncodedBlock.DIGEST_META, ContentEncodedBlock.CIPHER_META,
            ContentEncodedBlock.SIZE_META).createImmutable();

    private static ContentEncodedBlock parseVersion2(Map<String, String> meta,
            Block block) {
        if (!meta.keySet().equals(META)) {
            throw new IllegalArgumentException("meta missmatch");
        }

        if (!checkKeyType(meta)) {
            throw new IllegalArgumentException("wrong key-type");
        }

        if (!checkVersion(meta)) {
            throw new IllegalArgumentException("unknown version");
        }

        ContentLocatorKey key = getKey(meta);
        DigestProvider digest = getDigest(meta);
        CipherProvider cipher = getCipher(meta);
        Bytes body = block.getBody();

        return new ContentEncodedBlock(key, digest, cipher, body);
    }

    private static boolean checkKeyType(Map<String, String> meta) {
        String type = meta.get(ContentEncodedBlock.KEY_TYPE_META);
        return type.equals(KeyType.CONTENT_HASH);
    }

    private static boolean checkVersion(Map<String, String> meta) {
        String version = meta.get(ContentEncodedBlock.VERSION_META);
        return version.equals(ContentEncodedBlock.VERSION);
    }

    private static ContentLocatorKey getKey(Map<String, String> meta) {
        String hash = meta.get(ContentEncodedBlock.KEY_META);
        return LocatorKeyParse.parseContentLocatorKey(hash);
    }

    private static DigestProvider getDigest(Map<String, String> meta) {
        String name = meta.get(ContentEncodedBlock.DIGEST_META);
        return DigestFactory.getProvider(name);
    }

    private static CipherProvider getCipher(Map<String, String> meta) {
        String name = meta.get(ContentEncodedBlock.CIPHER_META);
        return CipherFactory.getProvider(name);
    }

    @ExternalName
    private static final Set<String> META_OLD = new SetBuilder<String>(
            ContentEncodedBlock.KEY_TYPE_META, ContentEncodedBlock.KEY_META,
            ContentEncodedBlock.DIGEST_META, ContentEncodedBlock.CIPHER_META,
            ContentEncodedBlock.SIZE_META).createImmutable();

    private static ContentEncodedBlock parseVersion1(Map<String, String> meta,
            Block block) {
        if (!meta.keySet().equals(META_OLD)) {
            throw new IllegalArgumentException("meta missmatch");
        }

        if (!checkKeyType(meta)) {
            throw new IllegalArgumentException("wrong key-type");
        }

        ContentLocatorKey key = getKey(meta);
        DigestProvider digest = getDigest(meta);
        CipherProvider cipher = getVersion1Cipher(meta);
        Bytes body = block.getBody();

        return new ContentEncodedBlock(key, digest, cipher, body);
    }

    private static CipherProvider getVersion1Cipher(Map<String, String> meta) {
        String name = meta.get(ContentEncodedBlock.CIPHER_META);
        name = fixCipher(name);
        return CipherFactory.getProvider(name);
    }

    private static String fixCipher(String cipher) {
        String name = CIPHER_FIX.get(cipher);
        if (name == null) {
            return cipher;
        }
        return name;
    }

    /**
     * translation map to fix cipher names between version 1 and 2.
     */
    @ExternalName
    private static final Map<String, String> CIPHER_FIX;

    static {
        Map<String, String> m = new HashMap<String, String>(2);
        m.put("aes-192-cbc", "rijndael192-192-cbc");
        m.put("aes-256-cbc", "rijndael256-256-cbc");
        CIPHER_FIX = Collections.unmodifiableMap(m);
    }
}
