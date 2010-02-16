package org.yi.happy.archive;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;

/**
 * A block store that uses a file system to store the blocks.
 */
public class FileBlockStore implements BlockStore {

    private final FileSystem fs;
    private final String base;

    /**
     * create a file system block store.
     * 
     * @param fs
     *            the file system to use.
     * @param base
     *            the base path to use.
     */
    public FileBlockStore(FileSystem fs, String base) {
	this.fs = fs;
	this.base = base;
    }

    public void put(EncodedBlock b) throws IOException {
	LocatorKey key = b.getKey();
	String name = Base16.encode(key.getHash()) + "-" + key.getType();

	fs.mkdir(base);
	String fileName = fs.join(base, name.substring(0, 1));
	fs.mkdir(fileName);
	fileName = fs.join(fileName, name.substring(0, 2));
	fs.mkdir(fileName);
	fileName = fs.join(fileName, name.substring(0, 3));
	fs.mkdir(fileName);
	fileName = fs.join(fileName, name);

	fs.save(fileName, b.asBytes());
    }

    @Override
    public EncodedBlock get(LocatorKey key) throws IOException {
	String name = Base16.encode(key.getHash()) + "-" + key.getType();

	String fileName = fs.join(base, name.substring(0, 1));
	fileName = fs.join(fileName, name.substring(0, 2));
	fileName = fs.join(fileName, name.substring(0, 3));
	fileName = fs.join(fileName, name);

	try {
	    return EncodedBlockParse.parse(fs.load(fileName));
	} catch (FileNotFoundException e) {
	    return null;
	} catch (IllegalArgumentException e) {
	    // TODO remove the corrupted file
	    throw new DecodeException(e);
	}
    }

}
