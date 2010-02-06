package org.yi.happy.archive.block.encoder;

import static org.yi.happy.archive.DigestUtil.digestData;

import java.security.MessageDigest;

import org.yi.happy.archive.BlockUtil;
import org.yi.happy.archive.Cipher;
import org.yi.happy.archive.CipherProvider;
import org.yi.happy.archive.DigestProvider;
import org.yi.happy.archive.EncodedBlockFactory;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.key.BlobFullKey;

public class BlockEncoderBlob implements BlockEncoder {
    public BlockEncoderBlob(DigestProvider digest, CipherProvider cipher) {
	this.digest = digest;
	this.cipher = cipher;
    }

    private final DigestProvider digest;
    private final CipherProvider cipher;

    /**
     * encode a content hash block
     * 
     * @param block
     *            the block to encode
     * @return the resulting block
     */
    public BlockEncoderResult encode(Block block) {
	byte[] body = block.asBytes();
	MessageDigest d = digest.get();

	byte[] ph = digestData(body, d);

	Cipher c = cipher.get();
	byte[] key = BlockUtil.expandKey(d, ph, c.getKeySize());
	c.setKey(key);

	body = pad(body, c.getBlockSize());
	c.encrypt(body);

	EncodedBlock out = EncodedBlockFactory.create(digest, cipher, body);

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
