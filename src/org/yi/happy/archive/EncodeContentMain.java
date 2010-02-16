package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.encoder.BlockEncoder;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.block.encoder.BlockEncoderResult;
import org.yi.happy.archive.block.parser.BlockParse;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;

/**
 * command line tool to encode a content block. The clear data is read from the
 * file named in the first argument, the encoded block is written to the file
 * named in the second argument, the full key for decoding is printed on
 * standard output.
 */
public class EncodeContentMain {
    private final FileSystem fs;
    private final Writer out;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	FileSystem fs = new RealFileSystem();
	Writer out = new OutputStreamWriter(System.out);

	new EncodeContentMain(fs, out).run(args);

	out.flush();
    }

    /**
     * 
     * @param fs
     *            the file system to use.
     * @param out
     *            where to send output.
     */
    public EncodeContentMain(FileSystem fs, Writer out) {
	this.fs = fs;
	this.out = out;
    }

    /**
     * encode a block.
     * 
     * @param args
     *            the file name of the block.
     * @throws IOException
     */
    @EntryPoint
    public void run(String... args) throws IOException {
	BlockEncoder encoder = BlockEncoderFactory.getContentDefault();

	Block block = BlockParse.parse(fs.load(args[0], Blocks.MAX_SIZE));
	BlockEncoderResult e = encoder.encode(block);
	fs.save(args[1], e.getBlock().asBytes());

	out.write(e.getKey() + "\n");
    }
}
