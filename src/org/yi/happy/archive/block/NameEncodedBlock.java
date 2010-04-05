package org.yi.happy.archive.block;

import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.annotate.BrokenContract;
import org.yi.happy.annotate.ExternalName;
import org.yi.happy.archive.BadSignatureException;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.block.parser.BlockParse;
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

    public DigestProvider getDigest() {
        return digest;
    }

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

    public Block decode(FullKey fullKey) {
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

        return BlockParse.parse(out);
    }

    @Override
    @BrokenContract(Block.class)
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + body.hashCode();
        result = prime * result + ((cipher == null) ? 0 : cipher.hashCode());
        result = prime * result + ((digest == null) ? 0 : digest.hashCode());
        result = prime * result + hash.hashCode();
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    @BrokenContract(Block.class)
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NameEncodedBlock other = (NameEncodedBlock) obj;
        if (!body.equals(other.body))
            return false;
        if (cipher == null) {
            if (other.cipher != null)
                return false;
        } else if (!cipher.equals(other.cipher))
            return false;
        if (digest == null) {
            if (other.digest != null)
                return false;
        } else if (!digest.equals(other.digest))
            return false;
        if (!hash.equals(other.hash))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }
}
