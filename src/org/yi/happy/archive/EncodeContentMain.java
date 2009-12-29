package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * command line tool to encode a content block. The clear data is read from the
 * file named in the first argument, the encoded block is written to the file
 * named in the second argument, the full key for decoding is printed on
 * standard output.
 */
public class EncodeContentMain {
	private final FileSystem fs;
	private final Writer out;

	public static void main(String[] args) throws Exception {
		FileSystem fs = new RealFileSystem();
		Writer out = new OutputStreamWriter(System.out);

		new EncodeContentMain(fs, out).run(args);

		out.flush();
	}

	public EncodeContentMain(FileSystem fs, Writer out) {
		this.fs = fs;
		this.out = out;
	}

	public void run(String... args) throws IOException,
			NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("sha-256");
		NamedCipher cipher = CipherFactory.createNamed("rijndael256-256-cbc");
		BlockEncoderContent encoder = new BlockEncoderContent(digest, cipher);
		Block block = BlockParse.load(fs.load(args[0], Blocks.MAX_SIZE));
		BlockEncoderResult e = encoder.encode(block);

		fs.save(args[1], e.getBlock().asBytes());

		out.write(e.getKey() + "\n");
	}
}