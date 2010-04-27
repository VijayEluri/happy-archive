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
    private final LineCache cache;

    public RandomLineReader(RandomAccessFile file, LineCache cache) {
        this.file = file;
        this.cache = cache;
    }

    public String getFirst() throws IOException {
        String line = cache.getFirst();
        if (line != null) {
            return line;
        }

        file.seek(0);
        line = file.readLine();

        cache.putFirst(line);

        return line;
    }

    public long length() throws IOException {
        return file.length();
    }

    public String get(long position) throws IOException {
        String line = cache.get(position);
        if (line != null) {
            return line;
        }

        file.seek(position);
        file.readLine();
        long found = file.getFilePointer();

        line = file.readLine();

        cache.put(position, found, line);
        return line;
    }

    public long getPosition(long position) throws IOException {
        file.seek(position);
        file.readLine();
        return file.getFilePointer();
    }

}
