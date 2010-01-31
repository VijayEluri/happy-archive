package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;

/**
 * put a block in a file store. Both the file store base directory and the block
 * as a file are named on the command line.
 */
public class FileStoreBlockPutMain {
    private final FileSystem fs;

    @EntryPoint
    public static void main(String[] args) throws IOException {
	RealFileSystem fs = new RealFileSystem();
	new FileStoreBlockPutMain(fs).run(args);
    }

    public FileStoreBlockPutMain(FileSystem fs) {
	this.fs = fs;
    }

    public void run(String... args) throws IOException {
	FileBlockStore store = new FileBlockStore(fs, args[0]);

	EncodedBlock b = new EncodedBlockParse().parse(fs.load(args[1],
		Blocks.MAX_SIZE));

	store.put(b);
    }
}
