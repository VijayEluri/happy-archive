package org.yi.happy.archive.block.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.key.KeyType;
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
    private static final Set<String> META;
    static {
        Set<String> m = new HashSet<String>();
        m.add(NameEncodedBlock.VERSION_META);
        m.add(NameEncodedBlock.KEY_TYPE_META);
        m.add(NameEncodedBlock.KEY_META);
        m.add(NameEncodedBlock.DIGEST_META);
        m.add(NameEncodedBlock.CIPHER_META);
        m.add(NameEncodedBlock.HASH_META);
        m.add(NameEncodedBlock.SIZE_META);
        META = Collections.unmodifiableSet(m);
    }

    private static NameEncodedBlock parseVersion2(Map<String, String> meta,
            Block block) {
        if (!meta.keySet().equals(META)) {
            throw new IllegalArgumentException("meta missmatch");
        }

        if (!meta.get(NameEncodedBlock.VERSION_META).equals(
                NameEncodedBlock.VERSION)) {
            throw new IllegalArgumentException("bad version");
        }

        if (!meta.get(NameEncodedBlock.KEY_TYPE_META).equals(KeyType.NAME_HASH)) {
            throw new IllegalArgumentException("wrong block type");
        }

        NameLocatorKey key = KeyParse.parseNameLocatorKey(meta
                .get(NameEncodedBlock.KEY_META));
        DigestProvider digest = DigestFactory.getProvider(meta
                .get(NameEncodedBlock.DIGEST_META));
        CipherProvider cipher = CipherFactory.getProvider(meta
                .get(NameEncodedBlock.CIPHER_META));
        HashValue hash = new HashValue(meta.get(NameEncodedBlock.HASH_META));
        int size = Integer.parseInt(meta.get(NameEncodedBlock.SIZE_META));
        Bytes body = block.getBody();

        if (body.getSize() != size) {
            throw new IllegalArgumentException("size missmatch");
        }

        return new NameEncodedBlock(key, hash, digest, cipher, body);
    }

    /**
     * The version one meta-data field set.
     */
    @ExternalName
    private static final Set<String> META_OLD;
    static {
        Set<String> m = new HashSet<String>();
        m.add(NameEncodedBlock.KEY_TYPE_META);
        m.add(NameEncodedBlock.KEY_META);
        m.add(NameEncodedBlock.DIGEST_META);
        m.add(NameEncodedBlock.CIPHER_META);
        m.add(NameEncodedBlock.HASH_META);
        m.add(NameEncodedBlock.SIZE_META);
        META_OLD = Collections.unmodifiableSet(m);
    }

    private static NameEncodedBlock parseVersion1(Map<String, String> meta,
            Block block) {
        if (!meta.keySet().equals(META_OLD)) {
            throw new IllegalArgumentException("meta missmatch");
        }

        if (!meta.get(NameEncodedBlock.KEY_TYPE_META).equals(KeyType.NAME_HASH)) {
            throw new IllegalArgumentException("wrong block type");
        }

        NameLocatorKey key = KeyParse.parseNameLocatorKey(meta
                .get(NameEncodedBlock.KEY_META));
        DigestProvider digest = DigestFactory.getProvider(meta
                .get(NameEncodedBlock.DIGEST_META));
        String cipher0 = meta.get(NameEncodedBlock.CIPHER_META);
        CipherProvider cipher = CipherFactory.getProvider(fixCipher(cipher0));
        HashValue hash = new HashValue(meta.get(NameEncodedBlock.HASH_META));
        int size = Integer.parseInt(meta.get(NameEncodedBlock.SIZE_META));
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
    @ExternalName
    public static final Map<String, String> fixMap;

    static {
        Map<String, String> m = new HashMap<String, String>(2);
        m.put("aes-192-cbc", "rijndael192-192-cbc");
        m.put("aes-256-cbc", "rijndael256-256-cbc");
        fixMap = Collections.unmodifiableMap(m);
    }
}
