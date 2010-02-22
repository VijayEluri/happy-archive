package org.yi.happy.archive.block;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.annotate.ExternalName;
import org.yi.happy.archive.Bytes;

/**
 * A value object for a generic block.
 */
public final class GenericBlock extends AbstractBlock implements Block {

    private final Bytes body;

    private final Map<String, String> meta;

    /**
     * the size meta-data field name.
     */
    @ExternalName
    public static final String SIZE_META = "size";

    /**
     * create, checking that the meta-data headers are valid.
     * 
     * @param meta
     *            the meta-data headers.
     * @param body
     *            the bytes of the body.
     */
    public GenericBlock(Map<String, String> meta, Bytes body) {
	for (Map.Entry<String, String> i : meta.entrySet()) {
	    checkHeader(i.getKey(), i.getValue());
	}

	if (body == null) {
	    body = new Bytes();
	}

	checkSize: {
	    String s = meta.get(SIZE_META);

	    if (s == null) {
		break checkSize;
	    }

	    int i;
	    try {
		i = Integer.parseInt(s);
	    } catch (NumberFormatException e) {
		throw new IllegalArgumentException("invalid size header");
	    }

	    if (i != body.getSize()) {
		throw new IllegalArgumentException("invalid size header");
	    }
	}

	this.meta = Collections
		.unmodifiableMap(new LinkedHashMap<String, String>(meta));
	this.body = body;
    }

    /**
     * create blank.
     */
    public GenericBlock() {
	this.meta = Collections.emptyMap();
	this.body = new Bytes();
    }

    /**
     * create a fully defined block.
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

	Map<String, String> m = new LinkedHashMap<String, String>();
	for (int i = 0; i < meta.length; i += 2) {
	    if (m.containsKey(meta[i])) {
		throw new IllegalArgumentException("repeated header: "
			+ meta[i]);
	    }
	    m.put(meta[i], meta[i + 1]);
	}

	return new GenericBlock(m, body);
    }

    /**
     * get the block meta data
     * 
     * @return the map of meta data fields.
     */
    @Override
    public Map<String, String> getMeta() {
	return meta;
    }

    /**
     * get the data block
     * 
     * @return the internal data block, never null.
     */
    @Override
    public Bytes getBody() {
	return body;
    }
}
