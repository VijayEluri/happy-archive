package org.yi.happy.archive.block;

import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.annotate.ExternalName;
import org.yi.happy.archive.BadSignatureException;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.block.parser.GenericBlockParse;
import org.yi.happy.archive.crypto.Cipher;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.crypto.Digests;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.HashValue;
import org.yi.happy.archive.key.NameFullKey;
import org.yi.happy.archive.key.NameLocatorKey;

/**
 * A name encoded block.
 */
public final class NameEncodedBlock extends AbstractBlock implements
        EncodedBlock {

    private final NameLocatorKey key;
    private final HashValue hash;
    private final DigestProvider digest;
    private final CipherProvider cipher;
    private final Bytes body;

    /**
     * create a name encoded block from all details.
     * 
     * @param key
     *            the key of the block.
     * @param hash
     *            the hash of the body of the block.
     * @param digest
     *            the digest to use.
     * @param cipher
     *            the cipher to use.
     * @param body
     *            the body of the block.
     * @throws IllegalArgumentException
     *             if the details do not agree.
     */
    public NameEncodedBlock(NameLocatorKey key, HashValue hash,
            DigestProvider digest, CipherProvider cipher, Bytes body) {
        checkHeader(DIGEST_META, digest.getAlgorithm());
        checkHeader(CIPHER_META, cipher.getAlgorithm());

        byte[] hash0 = ContentEncodedBlock.getHash(digest, body);
        if (!hash.equalBytes(hash0)) {
            throw new BadSignatureException();
        }

        this.key = key;
        this.hash = hash;
        this.digest = digest;
        this.cipher = cipher;
        this.body = body;
    }

    /**
     * create a name encoded block from minimal details.
     * 
     * @param key
     *            the key of the block.
     * @param digest
     *            the digest to use.
     * @param cipher
     *            the cipher to use.
     * @param body
     *            the body of the block.
     * @throws IllegalArgumentException
     *             if the details do not agree or are not usable.
     */
    public NameEncodedBlock(NameLocatorKey key, DigestProvider digest,
            CipherProvider cipher, Bytes body) {
        checkHeader(DIGEST_META, digest.getAlgorithm());
        checkHeader(CIPHER_META, cipher.getAlgorithm());

        byte[] hash = ContentEncodedBlock.getHash(digest, body);

        this.key = key;
        this.hash = new HashValue(hash);
        this.digest = digest;
        this.cipher = cipher;
        this.body = body;
    }

    @Override
    public NameLocatorKey getKey() {
        return key;
    }

    /**
     * get the hash of the body.
     * 
     * @return the hash of the body.
     */
    public HashValue getHash() {
        return hash;
    }

    @Override
    public DigestProvider getDigest() {
        return digest;
    }

    @Override
    public CipherProvider getCipher() {
        return cipher;
    }

    @Override
    public Bytes getBody() {
        return body;
    }

    /**
     * The version meta-data field name.
     */
    @ExternalName
    public static final String VERSION_META = "version";

    /**
     * The current version of this block type.
     */
    @ExternalName
    public static final String VERSION = "2";

    /**
     * The hash of the body meta-data field name.
     */
    @ExternalName
    public static final String HASH_META = "hash";

    @Override
    public Map<String, String> getMeta() {
        Map<String, String> out = new LinkedHashMap<String, String>();
        out.put(VERSION_META, VERSION);
        out.put(KEY_TYPE_META, key.getType());
        out.put(KEY_META, key.getHash().toString());
        out.put(HASH_META, hash.toString());
        out.put(DIGEST_META, digest.getAlgorithm());
        out.put(CIPHER_META, cipher.getAlgorithm());
        out.put(SIZE_META, Integer.toString(body.getSize()));
        return out;
    }

    @Override
    public GenericBlock decode(FullKey fullKey) {
        if (!fullKey.toLocatorKey().equals(key)) {
            throw new IllegalArgumentException("the key is not for this block");
        }
        NameFullKey k = (NameFullKey) fullKey;

        /*
         * get the cipher
         */
        Cipher c = cipher.get();

        /*
         * get the key from the name
         */
        DigestProvider algo = k.getDigest();
        byte[] part = ByteString.toUtf8(k.getName());

        c.setKey(Digests.expandKey(algo, part, c.getKeySize()));

        /*
         * decrypt the body
         */
        if (body.getSize() % c.getBlockSize() != 0) {
            throw new IllegalArgumentException(
                    "size is not a multiple of the cipher block size");
        }
        byte[] out = body.toByteArray();
        c.decrypt(out);

        return GenericBlockParse.parse(out);
    }
}
