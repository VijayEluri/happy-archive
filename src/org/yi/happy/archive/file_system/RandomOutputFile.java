package org.yi.happy.archive.file_system;

import java.io.IOException;

/**
 * A file that may be read and written at arbitrary positions within the byte
 * stream.
 */
public interface RandomOutputFile {
    void write(byte[] b, int offset, int length) throws IOException;

    void write(byte[] b) throws IOException;

    void write(int b) throws IOException;

    void close() throws IOException;

    long getPosition() throws IOException;

    void setPosition(long position) throws IOException;
}
