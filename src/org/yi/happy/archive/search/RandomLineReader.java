package org.yi.happy.archive.search;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Provides functionality to read lines from a random access file. Lines are
 * addressed by file offset, the first full line after the given offset is
 * returned on read.
 */
public class RandomLineReader {

    private final RandomAccessFile file;

    public RandomLineReader(RandomAccessFile file) {
        this.file = file;
    }

    public String getFirst() throws IOException {
        file.seek(0);
        return file.readLine();
    }

    public long length() throws IOException {
        return file.length();
    }

    public String get(long position) throws IOException {
        file.seek(position);
        file.readLine();
        return file.readLine();
    }

    public long getPosition(long position) throws IOException {
        file.seek(position);
        file.readLine();
        return file.getFilePointer();
    }

}
