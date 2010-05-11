package org.yi.happy.archive.block.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
        Map<String, String> meta = block.getMeta();

        if (!meta.containsKey(ContentEncodedBlock.KEY_TYPE_META)) {
            throw new MissingMetaException();
        }

        if (!meta.get(ContentEncodedBlock.KEY_TYPE_META).equals(
                KeyType.CONTENT_HASH)) {
            throw new IllegalArgumentException("wrong key-type");
        }

        String version = meta.get(ContentEncodedBlock.VERSION_META);
        if (version == null) {
            return parseVersion1(meta, block);
        }

        return parseVersion2(meta, block);
    }

    /**
     * The current version meta-data field set.
     */
    private static final Set<String> META;
    static {
        Set<String> m = new HashSet<String>();
        m.add(ContentEncodedBlock.VERSION_META);
        m.add(ContentEncodedBlock.KEY_TYPE_META);
        m.add(ContentEncodedBlock.KEY_META);
        m.add(ContentEncodedBlock.DIGEST_META);
        m.add(ContentEncodedBlock.CIPHER_META);
        m.add(ContentEncodedBlock.SIZE_META);
        META = Collections.unmodifiableSet(m);
    }

    private static ContentEncodedBlock parseVersion2(Map<String, String> meta,
            Block block) {
        if (!meta.keySet().equals(META)) {
            throw new IllegalArgumentException("meta missmatch");
        }

        if (!meta.get(ContentEncodedBlock.VERSION_META).equals(
                ContentEncodedBlock.VERSION)) {
            throw new IllegalArgumentException("unknown version");
        }

        ContentLocatorKey key = LocatorKeyParse.parseContentLocatorKey(meta
                .get(ContentEncodedBlock.KEY_META));
        DigestProvider digest = DigestFactory.getProvider(meta
                .get(ContentEncodedBlock.DIGEST_META));
        CipherProvider cipher = CipherFactory.getProvider(meta
                .get(ContentEncodedBlock.CIPHER_META));
        int size = Integer.parseInt(meta.get(ContentEncodedBlock.SIZE_META));
        Bytes body = block.getBody();

        if (size != body.getSize()) {
            throw new IllegalArgumentException("size missmatch");
        }

        return new ContentEncodedBlock(key, digest, cipher, body);
    }

    @ExternalName
    private static final Set<String> META_OLD;
    static {
        Set<String> m = new HashSet<String>();
        m.add(ContentEncodedBlock.KEY_TYPE_META);
        m.add(ContentEncodedBlock.KEY_META);
        m.add(ContentEncodedBlock.DIGEST_META);
        m.add(ContentEncodedBlock.CIPHER_META);
        m.add(ContentEncodedBlock.SIZE_META);
        META_OLD = Collections.unmodifiableSet(m);
    }

    private static ContentEncodedBlock parseVersion1(Map<String, String> meta,
            Block block) {
        if (!meta.keySet().equals(META_OLD)) {
            throw new IllegalArgumentException("meta missmatch");
        }

        ContentLocatorKey key = LocatorKeyParse.parseContentLocatorKey(meta
                .get(ContentEncodedBlock.KEY_META));
        DigestProvider digest = DigestFactory.getProvider(meta
                .get(ContentEncodedBlock.DIGEST_META));
        CipherProvider cipher = CipherFactory.getProvider(fixCipher(meta
                .get(ContentEncodedBlock.CIPHER_META)));
        int size = Integer.parseInt(meta.get(ContentEncodedBlock.SIZE_META));
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
    @ExternalName
    public static final Map<String, String> fixMap;

    static {
        Map<String, String> m = new HashMap<String, String>(2);
        m.put("aes-192-cbc", "rijndael192-192-cbc");
        m.put("aes-256-cbc", "rijndael256-256-cbc");
        fixMap = Collections.unmodifiableMap(m);
    }
}
