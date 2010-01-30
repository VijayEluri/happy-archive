package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.HexEncode;
import org.yi.happy.archive.key.LocatorKey;

public class FileBlockStore implements BlockStore {

    private final FileSystem fs;
    private final String base;

    public FileBlockStore(FileSystem fs, String base) {
	this.fs = fs;
	this.base = base;
    }

    public void put(EncodedBlock b) throws IOException {
	LocatorKey key = b.getKey();
	String name = HexEncode.encode(key.getHash()) + "-" + key.getType();

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

}
