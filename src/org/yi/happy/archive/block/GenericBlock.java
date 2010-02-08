package org.yi.happy.archive.block;

import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.archive.Bytes;

/**
 * A data object for a data block.
 */
public class GenericBlock extends AbstractBlock implements Block {

    private Bytes body = new Bytes();

    private final Map<String, String> meta;

    /**
     * create empty
     */
    public GenericBlock() {
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
    public static GenericBlock create(Bytes body, String... meta) {
	if (meta.length % 2 != 0) {
	    throw new IllegalArgumentException("meta needs to be pairs");
	}
	GenericBlock out = new GenericBlock();
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
    public void setBody(Bytes body) {
	if (body == null) {
	    body = new Bytes();
	}

	this.body = body;
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
	checkHeader(name, value);

	if (meta.containsKey(name)) {
	    throw new IllegalArgumentException("already have header: " + name);
	}
	meta.put(name, value);
    }

    /**
     * get the block meta data
     * 
     * @return the map of meta data fields.
     */
    @Override
    public Map<String, String> getMeta() {
	return new LinkedHashMap<String, String>(meta);
    }

    /**
     * get the data block
     * 
     * @return the internal data block, never null.
     */
    public Bytes getBody() {
	return body;
    }

    @Override
    public int hashCode() {
	final int PRIME = 31;
	int result = 1;
	result = PRIME * result + body.hashCode();
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
	final GenericBlock other = (GenericBlock) obj;
	if (!body.equals(other.body)) {
	    return false;
	}
	if (!meta.equals(other.meta)) {
	    return false;
	}
	return true;
    }

    /**
     * Check a meta-data header, throwing an {@link IllegalArgumentException} on
     * failure.
     * 
     * @param name
     *            the name part of the header.
     * @param value
     *            the value part of the header.
     */
    public static void checkHeader(String name, String value) {
	for (int i = 0; i < name.length(); i++) {
	    char c = name.charAt(i);
	    if (c == ':' || c == '\r' || c == '\n') {
		throw new IllegalArgumentException(
			"name can not have ':' or newline");
	    }
	}

	for (int i = 0; i < value.length(); i++) {
	    char c = value.charAt(i);
	    if (c == '\r' || c == '\n') {
		throw new IllegalArgumentException("value can not have newline");
	    }
	}
    }

}
