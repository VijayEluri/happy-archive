package org.yi.happy.archive.file_system;

import java.io.IOException;
import java.util.Arrays;

import org.yi.happy.archive.ClosedException;

public class FakeRandomOutputFile implements RandomOutputFile {

    public interface CloseListener {

	void onClose(byte[] bytes);

    }

    private byte[] bytes = new byte[0];

    private int position = 0;

    private boolean closed = false;

    private CloseListener closeListener = new CloseListener() {
	@Override
	public void onClose(byte[] bytes) {
	    // TODO Auto-generated method stub

	}
    };

    public FakeRandomOutputFile() {
	// TODO Auto-generated constructor stub
    }

    public FakeRandomOutputFile(byte[] bytes) {
	this.bytes = bytes;
    }

    @Override
    public void close() throws IOException {
	if (closed) {
	    return;
	}

	closed = true;

	closeListener.onClose(bytes);
    }

    @Override
    public void setPosition(long position) throws IOException {
	if (closed) {
	    throw new ClosedException();
	}

	if (position < 0) {
	    throw new IllegalArgumentException();
	}

	this.position = (int) position;
    }

    @Override
    public void write(byte[] b, int offset, int length) throws IOException {
	if (closed) {
	    throw new ClosedException();
	}

	if (position + length > bytes.length) {
	    bytes = Arrays.copyOf(bytes, position + length);
	}

	System.arraycopy(b, offset, bytes, position, length);

	position += length;
    }

    @Override
    public void write(byte[] b) throws IOException {
	if (closed) {
	    throw new ClosedException();
	}

	if (position + b.length > bytes.length) {
	    bytes = Arrays.copyOf(bytes, position + b.length);
	}

	System.arraycopy(b, 0, bytes, position, b.length);

	position += b.length;
    }

    @Override
    public void write(int b) throws IOException {
	if (closed) {
	    throw new ClosedException();
	}

	if (position + 1 > bytes.length) {
	    bytes = Arrays.copyOf(bytes, position + 1);
	}

	bytes[position] = (byte) b;

	position += 1;
    }

    public byte[] getBytes() {
	return bytes;
    }

    @Override
    public long getPosition() throws IOException {
	if (closed) {
	    throw new ClosedException();
	}

	return position;
    }

    public void setCloseListener(CloseListener closeListener) {
	this.closeListener = closeListener;
    }

}
