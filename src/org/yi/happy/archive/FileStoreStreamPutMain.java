package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.block.encoder.BlockEncoder;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;

/**
 * given the base name of a file store on the command line, and a stream on
 * stdin, store the stream, and print out the resulting key.
 */
public class FileStoreStreamPutMain {
    private FileSystem fs;
    private InputStream in;
    private Writer out;

    public FileStoreStreamPutMain(FileSystem fs, InputStream in, Writer out) {
	this.fs = fs;
	this.in = in;
	this.out = out;
    }

    public void run(String... args) throws IOException {
	BlockStore store = new FileBlockStore(fs, args[0]);
	BlockEncoder encoder = BlockEncoderFactory.getContentDefault();

	KeyOutputStream s = new KeyOutputStream(new StoreBlockStorage(encoder,
		store));

	Streams.copy(in, s);
	s.close();

	out.write(s.getFullKey() + "\n");
    }

    @EntryPoint
    public static void main(String[] args) throws IOException {
	FileSystem fs = new RealFileSystem();
	InputStream in = System.in;
	Writer out = new OutputStreamWriter(System.out, "UTF-8");

	new FileStoreStreamPutMain(fs, in, out).run(args);

	out.flush();
    }
}
