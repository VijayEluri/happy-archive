package org.yi.happy.archive.tag;

import java.io.IOException;

import org.yi.happy.archive.Fragment;
import org.yi.happy.archive.SplitReader;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RandomOutputFile;

/**
 * An incremental file restore process.
 */
public class RestoreFile {
    private final SplitReader data;
    private final String path;
    private final FileSystem fs;

    /**
     * initialize an incremental file restore process.
     * 
     * @param data
     *            the block source.
     * @param path
     *            the output file name.
     * @param fs
     *            the file system to use.
     */
    public RestoreFile(SplitReader data, String path, FileSystem fs) {
	this.data = data;
	this.path = path;
	this.fs = fs;
    }

    /**
     * performs a restore step: if there is data ready, write it out to the
     * named file.
     * 
     * @throws IOException
     */
    public void step() throws IOException {
	Fragment part = data.fetchAny();
	if (part == null) {
	    return;
	}

	RandomOutputFile f = fs.openRandomOutputFile(path);
	try {
	    while (part != null) {
		f.setPosition(part.getOffset());
		f.write(part.getData().toByteArray());

		part = data.fetchAny();
	    }
	} finally {
	    f.close();
	}
    }

    /**
     * check if the process is complete.
     * 
     * @return true if there is no more reading to do.
     */
    public boolean isDone() {
	return data.isDone();
    }

}
