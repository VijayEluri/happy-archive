package org.yi.happy.archive.block.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.yi.happy.annotate.ExternalName;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.NameEncodedBlock;
import org.yi.happy.archive.crypto.CipherFactory;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.key.HashValue;
import org.yi.happy.archive.key.KeyType;
import org.yi.happy.archive.key.LocatorKeyParse;
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
    public static NameEncodedBlock parse(Block block) {
        if (block instanceof NameEncodedBlock) {
            return (NameEncodedBlock) block;
        }

        Map<String, String> meta = block.getMeta();

        if (!meta.containsKey(NameEncodedBlock.VERSION_META)) {
            return parseVersion1(meta, block);
        }

        return parseVersion2(meta, block);
    }

    /**
     * The version two meta-data field set.
     */
    @ExternalName
    private static final Set<String> META = new SetBuilder<String>(
            NameEncodedBlock.VERSION_META, NameEncodedBlock.KEY_TYPE_META,
            NameEncodedBlock.KEY_META, NameEncodedBlock.DIGEST_META,
            NameEncodedBlock.CIPHER_META, NameEncodedBlock.HASH_META,
            NameEncodedBlock.SIZE_META).createImmutable();

    private static NameEncodedBlock parseVersion2(Map<String, String> meta,
            Block block) {
        if (!meta.keySet().equals(META)) {
            throw new IllegalArgumentException("meta missmatch");
        }

        if (!checkKeyType(meta)) {
            throw new IllegalArgumentException("wrong block type");
        }

        if (!checkVersion(meta)) {
            throw new IllegalArgumentException("bad version");
        }

        NameLocatorKey key = getKey(meta);
        DigestProvider digest = getDigest(meta);
        CipherProvider cipher = getCipher(meta);
        HashValue hash = getHash(meta);
        Bytes body = block.getBody();

        return new NameEncodedBlock(key, hash, digest, cipher, body);
    }

    private static boolean checkKeyType(Map<String, String> meta) {
        String type = meta.get(NameEncodedBlock.KEY_TYPE_META);
        return type.equals(KeyType.NAME_HASH);
    }

    private static boolean checkVersion(Map<String, String> meta) {
        String version = meta.get(NameEncodedBlock.VERSION_META);
        return version.equals(NameEncodedBlock.VERSION);
    }

    private static NameLocatorKey getKey(Map<String, String> meta) {
        String hash = meta.get(NameEncodedBlock.KEY_META);
        return LocatorKeyParse.parseNameLocatorKey(hash);
    }

    private static DigestProvider getDigest(Map<String, String> meta) {
        String name = meta.get(NameEncodedBlock.DIGEST_META);
        return DigestFactory.getProvider(name);
    }

    private static CipherProvider getCipher(Map<String, String> meta) {
        String name = meta.get(NameEncodedBlock.CIPHER_META);
        return CipherFactory.getProvider(name);
    }

    private static HashValue getHash(Map<String, String> meta) {
        String name = meta.get(NameEncodedBlock.HASH_META);
        return new HashValue(name);
    }

    /**
     * The version one meta-data field set.
     */
    @ExternalName
    private static final Set<String> META_OLD = new SetBuilder<String>(
            NameEncodedBlock.KEY_TYPE_META, NameEncodedBlock.KEY_META,
            NameEncodedBlock.DIGEST_META, NameEncodedBlock.CIPHER_META,
            NameEncodedBlock.HASH_META, NameEncodedBlock.SIZE_META)
            .createImmutable();

    private static NameEncodedBlock parseVersion1(Map<String, String> meta,
            Block block) {
        if (!meta.keySet().equals(META_OLD)) {
            throw new IllegalArgumentException("meta missmatch");
        }

        if (!checkKeyType(meta)) {
            throw new IllegalArgumentException("wrong block type");
        }

        NameLocatorKey key = getKey(meta);
        DigestProvider digest = getDigest(meta);
        CipherProvider cipher = getVersion1Cipher(meta);
        HashValue hash = getHash(meta);
        Bytes body = block.getBody();

        return new NameEncodedBlock(key, hash, digest, cipher, body);
    }

    private static CipherProvider getVersion1Cipher(Map<String, String> meta) {
        String name = meta.get(NameEncodedBlock.CIPHER_META);
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
