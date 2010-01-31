package org.yi.happy.archive;

import static org.yi.happy.archive.DigestUtil.digestData;

import java.security.MessageDigest;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.key.BlobFullKey;

public class BlockEncoderBlob implements BlockEncoder {
    public BlockEncoderBlob(MessageDigest digest, NamedCipher cipher) {
	this.digest = digest;
	this.cipher = cipher;
    }

    private final MessageDigest digest;
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

	byte[] ph = digestData(body, digest);

	byte[] key = BlockUtil.expandKey(digest, ph, cipher.getKeySize());
	cipher.setPass(key);

	body = pad(body, cipher.getBlockSize());
	cipher.encrypt(body);

	EncodedBlock out = EncodedBlockFactory.create(digest.getAlgorithm(),
		cipher.getAlgorithm(), body);

	BlobFullKey fullKey = new BlobFullKey(out.getKey().getHash(), key);

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
