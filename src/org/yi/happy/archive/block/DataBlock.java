package org.yi.happy.archive.block;

import java.util.Collections;
import java.util.Map;

import org.yi.happy.archive.Bytes;

/**
 * A simple data block. This implementation does not make a defensive copy of
 * the body.
 */
public class DataBlock extends AbstractBlock {
    private final Bytes body;

    /**
     * create a simple data block.
     * 
     * @param body
     *            the body of the block.
     */
    public DataBlock(Bytes body) {
        if (body == null) {
            throw new NullPointerException();
        }

        this.body = body;
    }

    @Override
    public Bytes getBody() {
        return body;
    }

    @Override
    public Map<String, String> getMeta() {
        return Collections.singletonMap("size", Integer
                .toString(body.getSize()));
    }
}
