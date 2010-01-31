package org.yi.happy.archive.block;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.yi.happy.archive.Blocks;
import org.yi.happy.archive.ByteString;

/**
 * The part that is common for all the block types.
 */
public abstract class AbstractBlock implements Block {
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

	    byte[] endl = Blocks.getEndl();

	    for (Map.Entry<String, String> i : getMeta().entrySet()) {
		String l = i.getKey() + ": " + i.getValue();
		out.write(ByteString.toUtf8(l));
		out.write(endl);
	    }

	    out.write(endl);

	    out.write(getBody());

	    return out.toByteArray();
	} catch (IOException e) {
	    throw new Error(e);
	}
    }

    @Override
    public abstract Map<String, String> getMeta();

    @Override
    public abstract byte[] getBody();
}
