package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;

/**
 * token parsing algorithm for binary streams. used to read a stream up to the
 * next instance of a set of tokens
 */
public class BinaryStreamParser {

    private byte[] buff;

    private byte[] found;

    private byte[] term;

    private boolean atEnd;

    /**
     * read until one of the given terminals is found
     * 
     * @param in
     *            the stream to read
     * @param terms
     *            the terminals to find
     * @return the bytes read before the terminal
     * @throws IOException
     */
    public byte[] readTo(InputStream in, byte[]... terms) throws IOException {
	atEnd = false;
	found = null;
	term = null;
	if (buff == null) {
	    buff = new byte[1024];
	}
	int used = 0;

	outer: while (true) {
	    int r = in.read(buff, used, 1);
	    if (r < 1) {
		atEnd = true;
		break outer;
	    }

	    used++;
	    term: for (byte[] term : terms) {
		if (used < term.length) {
		    continue term;
		}
		for (int i = 0; i < term.length; i++) {
		    if (buff[used - term.length + i] != term[i]) {
			continue term;
		    }
		}
		this.term = term.clone();
		used -= term.length;
		break outer;
	    }
	    if (used == buff.length) {
		byte[] tmp = new byte[used + 1024];
		System.arraycopy(buff, 0, tmp, 0, used);
		buff = tmp;
	    }
	}
	byte[] out = new byte[used];
	System.arraycopy(buff, 0, out, 0, used);
	found = out;
	return out;
    }

    /**
     * get the last found terminal
     * 
     * @return the terminal
     */
    public byte[] getTerminal() {
	return term;
    }

    /**
     * did the last readTo hit the end of the stream?
     * 
     * @return true if the end of the stream was hit
     */
    public boolean atEnd() {
	return atEnd;
    }

    /**
     * get the last found bytes
     * 
     * @return the bytes found before the terminal in the last call to readTo
     */
    public byte[] getFound() {
	return found;
    }
}
