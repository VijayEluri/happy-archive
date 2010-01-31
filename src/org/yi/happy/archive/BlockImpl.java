package org.yi.happy.archive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A data object for a data block.
 */
public class BlockImpl implements Block {

    private byte[] body;

    private final Map<String, String> meta;

    /**
     * create empty
     */
    public BlockImpl() {
	meta = new LinkedHashMap<String, String>();
    }

    /**
     * create a fully defined block
     * 
     * @param body
     *            the data part of the block
     * @param meta
     *            the meta headers, as pairs of names and values, must be an
     *            even number of elements
     * @return the created block
     */
    public static BlockImpl create(byte[] body, String... meta) {
	if (meta.length % 2 != 0) {
	    throw new IllegalArgumentException("meta needs to be pairs");
	}
	BlockImpl out = new BlockImpl();
	out.setBody(body);
	for (int i = 0; i < meta.length; i += 2) {
	    out.addMeta(meta[i], meta[i + 1]);
	}
	return out;
    }

    /**
     * set the data part of the block.
     * 
     * @param body
     *            the data block
     */
    public void setBody(byte[] body) {
	this.body = body;
    }

    private static final byte ENDL[] = { 0x0d, 0x0a };

    /**
     * convert to a block of bytes.
     * 
     * @return the binary representation
     */
    public byte[] asBytes() {
	ByteArrayOutputStream s = new ByteArrayOutputStream();

	try {
	    writeTo(s);
	} catch (IOException e) {
	    throw new Error(e);
	}

	return s.toByteArray();
    }

    /**
     * write the block to an output stream.
     * 
     * @param s
     *            the stream to write to
     * @throws IOException
     *             on IO errors
     */
    public void writeTo(OutputStream s) throws IOException {
	for (Map.Entry<String, String> i : meta.entrySet()) {
	    String out = i.getKey() + ": " + i.getValue();
	    s.write(ByteString.toUtf8(out));
	    s.write(ENDL);
	}

	s.write(ENDL);

	if (body != null) {
	    s.write(body);
	}
    }

    /**
     * add a meta-data header. The name may not be repeated.
     * 
     * @param name
     *            the name of the header, may only contain 7 bit ASCII, no new
     *            line characters, and no ": ".
     * @param value
     *            the value of the header, may only contain 7 bit ASCII, and no
     *            new line characters.
     * @throws IllegalArgumentException
     *             if the name or value are not valid.
     * @throws IllegalArgumentException
     *             if a header by this name was already added.
     */
    public void addMeta(String name, String value) {
	if (name.contains(": ")) {
	    throw new IllegalArgumentException("name may not contain ': '");
	}
	for (int i = 0; i < name.length(); i++) {
	    char c = name.charAt(i);
	    if (c > 0x7f || c < 1 || c == 0x0d || c == 0x0a) {
		throw new IllegalArgumentException("bad character in name: "
			+ (int) c);
	    }
	}
	for (int i = 0; i < value.length(); i++) {
	    char c = value.charAt(i);
	    if (c > 0x7f || c < 1 || c == 0x0d || c == 0x0a) {
		throw new IllegalArgumentException("bad character in value: "
			+ (int) c);
	    }
	}

	if (meta.containsKey(name)) {
	    throw new IllegalArgumentException("already have header: " + name);
	}
	meta.put(name, value);
    }

    /**
     * get the first meta value with a given name
     * 
     * @param name
     *            the name to look for
     * @return the first value found, or null if none found
     */
    public String getMeta(String name) {
	return meta.get(name);
    }

    /**
     * get the data block
     * 
     * @return the internal data block
     */
    public byte[] getBody() {
	return body;
    }

    @Override
    public int hashCode() {
	final int PRIME = 31;
	int result = 1;
	result = PRIME * result + Arrays.hashCode(body);
	result = PRIME * result + meta.hashCode();
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final BlockImpl other = (BlockImpl) obj;
	if (!Arrays.equals(body, other.body)) {
	    return false;
	}
	if (!meta.equals(other.meta)) {
	    return false;
	}
	return true;
    }

    /*
     * XXX this is a bad name, something like validation of being allowed in the
     * value part of a meta-field is what it should read as.
     */
    public static void checkValue(String value) {
	for (int i = 0; i < value.length(); i++) {
	    char c = value.charAt(i);
	    if (c > 0x7f || c < 1 || c == 0x0d || c == 0x0a) {
		throw new VerifyException("bad character in value: " + (int) c);
	    }
	}
    }

}
