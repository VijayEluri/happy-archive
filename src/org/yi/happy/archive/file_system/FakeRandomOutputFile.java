package org.yi.happy.archive.file_system;

import java.io.IOException;
import java.util.Arrays;

import org.yi.happy.archive.ClosedException;

/**
 * an in memory random output file, for testing.
 */
public class FakeRandomOutputFile implements RandomOutputFile {

    /**
     * listener for receiving notifications of the file being closed.
     */
    public interface CloseListener {

	/**
	 * The file was closed.
	 * 
	 * @param bytes
	 *            the new content of the file.
	 */
	void onClose(byte[] bytes);

    }

    private byte[] bytes;

    private int position = 0;

    private boolean closed = false;

    private CloseListener closeListener = new CloseListener() {
	@Override
	public void onClose(byte[] bytes) {

	}
    };

    /**
     * create empty.
     */
    public FakeRandomOutputFile() {
	this(new byte[0]);
    }

    /**
     * create with initial state.
     * 
     * @param bytes
     *            the initial state. the buffer is not defensively copied.
     */
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

    /**
     * get the current state of the buffer.
     * 
     * @return the buffer. the buffer is not defensively copied.
     */
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

    /**
     * set the listener that will be notified when the file is closed.
     * 
     * @param closeListener
     *            the new listener.
     */
    public void setCloseListener(CloseListener closeListener) {
	this.closeListener = closeListener;
    }

}
