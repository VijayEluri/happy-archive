package org.yi.happy.archive.file_system;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RealRandomOutputFile implements RandomOutputFile {
    private RandomAccessFile f;

    public RealRandomOutputFile(String name) throws IOException {
	f = new RandomAccessFile(name, "rw");
    }

    @Override
    public void close() throws IOException {
	f.close();
    }

    @Override
    public long getPosition() throws IOException {
	return f.getFilePointer();
    }

    @Override
    public void setPosition(long position) throws IOException {
	f.seek(position);
    }

    @Override
    public void write(byte[] b, int offset, int length) throws IOException {
	f.write(b, offset, length);
    }

    @Override
    public void write(byte[] b) throws IOException {
	f.write(b);
    }

    @Override
    public void write(int b) throws IOException {
	f.write(b);
    }
}
