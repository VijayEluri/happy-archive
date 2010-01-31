package org.yi.happy.archive.block;

import java.util.Collections;
import java.util.Map;

/**
 * A simple data block. This implementation does not make a defensive copy of
 * the body.
 */
public class DataBlock extends AbstractBlock {
    private final byte[] body;

    public DataBlock(byte[] body) {
	if (body == null) {
	    throw new NullPointerException();
	}

	this.body = body;
    }

    @Override
    public byte[] getBody() {
	return body;
    }

    @Override
    public Map<String, String> getMeta() {
	return Collections.singletonMap("size", Integer.toString(body.length));
    }
}
