package org.yi.happy.archive.block.parser;

import java.util.Map;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.Sets;
import org.yi.happy.archive.block.BlobEncodedBlock;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.crypto.CipherFactory;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.key.BlobLocatorKey;
import org.yi.happy.archive.key.KeyParse;

public class BlobEncodedBlockParse {

    @MagicLiteral
    public static BlobEncodedBlock parse(Block block) {
	if (block instanceof BlobEncodedBlock) {
	    return (BlobEncodedBlock) block;
	}

	Map<String, String> meta = block.getMeta();

	if (!meta.keySet().equals(
		Sets.asSet("key-type", "key", "digest", "cipher", "size"))) {
	    throw new IllegalArgumentException("meta missmatch");
	}

	if (!meta.get("key-type").equals("blob")) {
	    throw new IllegalArgumentException("wrong key-type");
	}

	BlobLocatorKey key = KeyParse.parseBlobLocatorKey(meta.get("key"));
	DigestProvider digest = DigestFactory.getProvider(meta.get("digest"));
	CipherProvider cipher = CipherFactory.getProvider(meta.get("cipher"));
	int size = Integer.parseInt(meta.get("size"));

	Bytes body = block.getBody();
	if (body.getSize() != size) {
	    throw new IllegalArgumentException("size missmatch");
	}

	return new BlobEncodedBlock(key, digest, cipher, body);
    }
}
