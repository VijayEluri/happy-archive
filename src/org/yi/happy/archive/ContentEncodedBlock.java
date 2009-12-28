package org.yi.happy.archive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;

import org.yi.happy.archive.key.ContentFullKey;
import org.yi.happy.archive.key.ContentLocatorKey;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.HexEncode;
import org.yi.happy.archive.key.UnknownAlgorithmException;

public final class ContentEncodedBlock implements EncodedBlock {
    private final ContentLocatorKey key;
    private final String digest;
    private final String cipher;
    private final byte[] body;

    public ContentEncodedBlock(ContentLocatorKey key, String digest,
            String cipher, byte[] body) {

        BlockImpl.checkValue(digest);
        BlockImpl.checkValue(cipher);

        byte[] hash = getHash(digest, body);
        if (!Arrays.equals(key.getHash(), hash)) {
            throw new IllegalArgumentException();
        }

        this.key = key;
        this.digest = digest;
        this.cipher = cipher;
        this.body = body.clone();
    }

    public ContentEncodedBlock(String digest, String cipher, byte[] body) {

        BlockImpl.checkValue(digest);
        BlockImpl.checkValue(cipher);

        byte[] hash = getHash(digest, body);

        this.key = new ContentLocatorKey(hash);
        this.digest = digest;
        this.cipher = cipher;
        this.body = body.clone();
    }

    public ContentLocatorKey getKey() {
        return key;
    }

    public String getDigest() {
        return digest;
    }

    public String getCipher() {
        return cipher;
    }

    public int getSize() {
        return body.length;
    }

    public byte[] getBody() {
        return body.clone();
    }

    public String getMeta(String name) {
        if (name.equals("version")) {
            return "2";
        }

        if (name.equals("key-type")) {
            return key.getType();
        }

        if (name.equals("key")) {
            return HexEncode.encode(key.getHash());
        }

        if (name.equals("digest")) {
            return digest;
        }

        if (name.equals("cipher")) {
            return cipher;
        }

        if (name.equals("size")) {
            return Integer.toString(body.length);
        }

        return null;
    }

    public byte[] asBytes() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            out.write(ByteString.toUtf8("version: 2\r\nkey-type: "));
            out.write(ByteString.toUtf8(key.getType()));
            out.write(ByteString.toUtf8("\r\nkey: "));
            out.write(ByteString.toUtf8(HexEncode.encode(key.getHash())));
            out.write(ByteString.toUtf8("\r\ndigest: "));
            out.write(ByteString.toUtf8(digest));
            out.write(ByteString.toUtf8("\r\ncipher: "));
            out.write(ByteString.toUtf8(cipher));
            out.write(ByteString.toUtf8("\r\nsize: "));
            out.write(ByteString.toUtf8(Integer.toString(body.length)));
            out.write(ByteString.toUtf8("\r\n\r\n"));
            out.write(body);

            return out.toByteArray();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public int getRawSize() {
        return asBytes().length;
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
     */
    public static byte[] getHash(String digest, byte[] body) {
        try {
            MessageDigest d = DigestFactory.create(digest);
            d.update(body);
            return d.digest();
        } catch (UnknownAlgorithmException e) {
            throw new UnknownDigestException(digest, e);
        }
    }

    public Block decode(FullKey fullKey) {
        if (!(fullKey instanceof ContentFullKey)) {
            throw new IllegalArgumentException();
        }
        ContentFullKey k = (ContentFullKey) fullKey;

        Cipher c = CipherFactory.create(this.cipher);
        c.setPass(k.getPass());

        if (body.length % c.getBlockSize() != 0) {
            throw new IllegalArgumentException(
                    "size is not a multiple of the cipher block size");
        }

        byte[] out = body.clone();
        c.decrypt(out);

        return BlockParse.load(out);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(body);
        result = prime * result + ((cipher == null) ? 0 : cipher.hashCode());
        result = prime * result + ((digest == null) ? 0 : digest.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
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
        if (!Arrays.equals(body, other.body))
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
