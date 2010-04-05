package org.yi.happy.archive.block.parser;

import java.util.Collections;
import java.util.HashSet;
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
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.key.KeyType;

/**
 * parser for a {@link BlobEncodedBlock}.
 */
public class BlobEncodedBlockParse {

    /**
     * The set of meta-data field names.
     */
    private static final Set<String> META;
    static {
        Set<String> m = new HashSet<String>();
        m.add(BlobEncodedBlock.KEY_TYPE_META);
        m.add(BlobEncodedBlock.KEY_META);
        m.add(BlobEncodedBlock.DIGEST_META);
        m.add(BlobEncodedBlock.CIPHER_META);
        m.add(BlobEncodedBlock.SIZE_META);
        META = Collections.unmodifiableSet(m);
    }

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

        if (!meta.get(BlobEncodedBlock.KEY_TYPE_META).equals(KeyType.BLOB)) {
            throw new IllegalArgumentException("wrong key-type");
        }

        BlobLocatorKey key = KeyParse.parseBlobLocatorKey(meta
                .get(BlobEncodedBlock.KEY_META));
        DigestProvider digest = DigestFactory.getProvider(meta
                .get(BlobEncodedBlock.DIGEST_META));
        CipherProvider cipher = CipherFactory.getProvider(meta
                .get(BlobEncodedBlock.CIPHER_META));
        int size = Integer.parseInt(meta.get(BlobEncodedBlock.SIZE_META));

        Bytes body = block.getBody();
        if (body.getSize() != size) {
            throw new IllegalArgumentException("size missmatch");
        }

        return new BlobEncodedBlock(key, digest, cipher, body);
    }
}
