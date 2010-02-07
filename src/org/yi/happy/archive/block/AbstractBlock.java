package org.yi.happy.archive.block;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;

/**
 * The part that is common for all the block types.
 */
public abstract class AbstractBlock implements Block {
    private byte[] ENDL = new byte[] { '\r', '\n' };
    private byte[] SEPARATOR = new byte[] { ':', ' ' };

    /**
     * get the block representation as bytes, assuming all the meta entries are
     * valid and the body is not null.
     */
    @Override
    public byte[] asBytes() {
	/*
	 * This can probably be done more efficiently, and maybe even more
	 * simply, without the stream.
	 */
	try {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();

	    for (Map.Entry<String, String> i : getMeta().entrySet()) {
		out.write(ByteString.toUtf8(i.getKey()));
		out.write(SEPARATOR);
		out.write(ByteString.toUtf8(i.getValue()));
		out.write(ENDL);
	    }

	    out.write(ENDL);

	    out.write(getBody().toByteArray());

	    return out.toByteArray();
	} catch (IOException e) {
	    throw new Error(e);
	}
    }

    @Override
    public abstract Map<String, String> getMeta();

    @Override
    public abstract Bytes getBody();
}
