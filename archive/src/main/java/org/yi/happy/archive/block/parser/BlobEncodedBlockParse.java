package org.yi.happy.archive.block.parser;

import java.util.Map;
import java.util.Set;

import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.block.BlobEncodedBlock;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.crypto.CipherFactory;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.key.BlobLocatorKey;
import org.yi.happy.archive.key.KeyType;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * parser for a {@link BlobEncodedBlock}.
 */
public class BlobEncodedBlockParse {

    /**
     * The set of meta-data field names.
     */
    private static final Set<String> META = new SetBuilder<String>(
            BlobEncodedBlock.KEY_TYPE_META, BlobEncodedBlock.KEY_META,
            BlobEncodedBlock.DIGEST_META, BlobEncodedBlock.CIPHER_META,
            BlobEncodedBlock.SIZE_META).createImmutable();

    /**
     * Parse a BlobEncodedBlock.
     * 
     * @param block
     *            the block to parse.
     * @return the parsed block.
     * @throws IllegalArgumentException
     *             on parsing failure.
     */
    public static BlobEncodedBlock parse(Block block)
            throws IllegalArgumentException {
        if (block instanceof BlobEncodedBlock) {
            return (BlobEncodedBlock) block;
        }

        Map<String, String> meta = block.getMeta();

        if (!meta.keySet().equals(META)) {
            throw new IllegalArgumentException("meta missmatch");
        }

        if (!checkKeyType(meta)) {
            throw new IllegalArgumentException("wrong key-type");
        }

        BlobLocatorKey key = getKey(meta);
        DigestProvider digest = getDigest(meta);
        CipherProvider cipher = getCipher(meta);
        Bytes body = block.getBody();

        return new BlobEncodedBlock(key, digest, cipher, body);
    }

    private static boolean checkKeyType(Map<String, String> meta) {
        String type = meta.get(BlobEncodedBlock.KEY_TYPE_META);
        return type.equals(KeyType.BLOB);
    }

    private static BlobLocatorKey getKey(Map<String, String> meta) {
        String hash = meta.get(BlobEncodedBlock.KEY_META);
        return LocatorKeyParse.parseBlobLocatorKey(hash);
    }

    private static DigestProvider getDigest(Map<String, String> meta) {
        String name = meta.get(BlobEncodedBlock.DIGEST_META);
        return DigestFactory.getProvider(name);
    }

    private static CipherProvider getCipher(Map<String, String> meta) {
        String name = meta.get(BlobEncodedBlock.CIPHER_META);
        return CipherFactory.getProvider(name);
    }
}
