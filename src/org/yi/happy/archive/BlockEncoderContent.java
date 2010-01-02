package org.yi.happy.archive;

import java.security.MessageDigest;

import org.yi.happy.archive.key.ContentFullKey;

/**
 * A block encoder for content hash blocks.
 */
public class BlockEncoderContent implements BlockEncoder {
    public BlockEncoderContent(MessageDigest digest, NamedCipher cipher) {
	this.digest = digest;
	this.cipher = cipher;
    }

    /**
     * the digest to use
     */
    private final MessageDigest digest;

    /**
     * the cipher to use
     */
    private final NamedCipher cipher;

    /**
     * encode a content hash block
     * 
     * @param block
     *            the block to encode
     * @return the resulting block
     */
    public BlockEncoderResult encode(Block block) {
	byte[] body = block.asBytes();

	byte[] ph = DigestUtil.digestData(body, digest);

	byte[] key = BlockUtil.expandKey(digest, ph, cipher.getKeySize());
	cipher.setPass(key);

	body = pad(body, cipher.getBlockSize());
	cipher.encrypt(body);

	EncodedBlock out = EncodedBlockFactory.createContent(digest
		.getAlgorithm(), cipher.getAlgorithm(), body);

	ContentFullKey fullKey = new ContentFullKey(out.getKey().getHash(), key);

	return new BlockEncoderResult(fullKey, out);
    }

    /**
     * pad a byte array up to the given block size
     * 
     * @param block
     *            the block to pad
     * @param blockSize
     *            the block size to pad to
     * @return block if it the right size, or a new byte array that is padded
     *         with zeros to blockSize
     */
    private static byte[] pad(byte[] block, int blockSize) {
	int slack = block.length % blockSize;

	if (slack == 0) {
	    return block;
	}

	byte[] out = new byte[block.length + blockSize - slack];
	System.arraycopy(block, 0, out, 0, block.length);
	return out;
    }
}
