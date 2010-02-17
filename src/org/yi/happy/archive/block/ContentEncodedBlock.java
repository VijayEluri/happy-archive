package org.yi.happy.archive.block;

import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.annotate.ExternalName;
import org.yi.happy.archive.Base16;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.UnknownDigestAlgorithmException;
import org.yi.happy.archive.block.parser.BlockParse;
import org.yi.happy.archive.crypto.Cipher;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.crypto.Digests;
import org.yi.happy.archive.key.ContentFullKey;
import org.yi.happy.archive.key.ContentLocatorKey;
import org.yi.happy.archive.key.FullKey;

/**
 * A content encoded block.
 */
public final class ContentEncodedBlock extends AbstractBlock implements
	EncodedBlock {
    private final ContentLocatorKey key;
    private final DigestProvider digest;
    private final CipherProvider cipher;
    private final Bytes body;

    /**
     * The name of the digest meta-data header.
     */
    @ExternalName
    public static final String DIGEST = "digest";

    /**
     * The name of the cipher meta-data header.
     */
    @ExternalName
    public static final String CIPHER = "cipher";

    /**
     * The name of the version meta-data header.
     */
    @ExternalName
    public static final String VERSION = "version";

    /**
     * The name of the current version.
     */
    @ExternalName
    public static final String VERSION_TWO = "2";

    /**
     * The name of the key type meta-data header.
     */
    @ExternalName
    public static final String KEY_TYPE = "key-type";

    /**
     * The name of the key meta-data header.
     */
    @ExternalName
    public static final String KEY = "key";

    /**
     * The name of the size meta-data header.
     */
    @ExternalName
    public static final String SIZE = "size";

    /**
     * create a content encoded block with all details.
     * 
     * @param key
     *            the locator key for the block.
     * @param digest
     *            the digest to use.
     * @param cipher
     *            the cipher to use.
     * @param body
     *            the body of the block.
     * @throws IllegalArgumentException
     *             if the details do not check out.
     */
    public ContentEncodedBlock(ContentLocatorKey key, DigestProvider digest,
	    CipherProvider cipher, Bytes body) {

	GenericBlock.checkHeader(DIGEST, digest.getAlgorithm());
	GenericBlock.checkHeader(CIPHER, cipher.getAlgorithm());

	byte[] hash = getHash(digest, body);
	if (!key.getHash().equalBytes(hash)) {
	    throw new IllegalArgumentException();
	}

	this.key = key;
	this.digest = digest;
	this.cipher = cipher;
	this.body = body;
    }

    /**
     * create a content encoded block with minimal details.
     * 
     * @param digest
     *            the digest to use.
     * @param cipher
     *            the cipher to use.
     * @param body
     *            the body of the block.
     * @throws IllegalArgumentException
     *             if the details do not make sense.
     */
    public ContentEncodedBlock(DigestProvider digest, CipherProvider cipher,
	    Bytes body) {

	GenericBlock.checkHeader(DIGEST, digest.getAlgorithm());
	GenericBlock.checkHeader(CIPHER, cipher.getAlgorithm());

	byte[] hash = getHash(digest, body);

	this.key = new ContentLocatorKey(new Bytes(hash));
	this.digest = digest;
	this.cipher = cipher;
	this.body = body;
    }

    public ContentLocatorKey getKey() {
	return key;
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

    @Override
    public Map<String, String> getMeta() {
	Map<String, String> out = new LinkedHashMap<String, String>();
	out.put(VERSION, VERSION_TWO);
	out.put(KEY_TYPE, key.getType());
	out.put(KEY, Base16.encode(key.getHash()));
	out.put(DIGEST, digest.getAlgorithm());
	out.put(CIPHER, cipher.getAlgorithm());
	out.put(SIZE, Integer.toString(body.getSize()));
	return out;
    }

    /**
     * get the content hash for a block.
     * 
     * @param digest
     *            the normalized digest name.
     * @param cipher
     *            the normalized cipher name.
     * @param body
     *            the body.
     * @return the hash value.
     * @throws UnknownDigestAlgorithmException
     *             if the implementation for the digest is unknown.
     */
    public static byte[] getHash(DigestProvider digest, Bytes body)
	    throws UnknownDigestAlgorithmException {
	return Digests.digestData(digest, body);
    }

    public Block decode(FullKey fullKey) {
	if (!fullKey.toLocatorKey().equals(key)) {
	    throw new IllegalArgumentException("the key is not for this block");
	}
	ContentFullKey k = (ContentFullKey) fullKey;

	Cipher c = cipher.get();
	c.setKey(k.getPass().toByteArray());

	if (body.getSize() % c.getBlockSize() != 0) {
	    throw new IllegalArgumentException(
		    "size is not a multiple of the cipher block size");
	}

	byte[] out = body.toByteArray();
	c.decrypt(out);

	return BlockParse.parse(out);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + body.hashCode();
	result = prime * result + cipher.hashCode();
	result = prime * result + digest.hashCode();
	result = prime * result + key.hashCode();
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ContentEncodedBlock other = (ContentEncodedBlock) obj;
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
	if (key == null) {
	    if (other.key != null)
		return false;
	} else if (!key.equals(other.key))
	    return false;
	return true;
    }
}
