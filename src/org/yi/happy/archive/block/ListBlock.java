package org.yi.happy.archive.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yi.happy.annotate.ExternalName;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.BytesBuilder;
import org.yi.happy.archive.key.FullKey;

/**
 * A block that is just a list of keys.
 */
public class ListBlock extends AbstractBlock implements Block {

    private final List<FullKey> entries;

    private final int size;

    /**
     * Create from a list of keys.
     * 
     * @param entries
     *            the keys.
     */
    public ListBlock(List<FullKey> entries) {
        entries = new ArrayList<FullKey>(entries);
        this.entries = Collections.unmodifiableList(entries);

        int size = 0;
        for (FullKey entry : entries) {
            byte[] k = ByteString.toUtf8(entry.toString());
            size += k.length + 1;
        }
        this.size = size;
    }

    /**
     * the byte that separates records in the block.
     */
    public static final byte RECORD_SEPARATOR = '\n';

    @Override
    public Bytes getBody() {
        BytesBuilder out = new BytesBuilder();
        for (FullKey entry : entries) {
            out.add(ByteString.toUtf8(entry.toString())).add(RECORD_SEPARATOR);
        }
        return out.create();
    }

    /**
     * the meta-data field name for type.
     */
    @ExternalName
    public static final String TYPE_META = "type";

    /**
     * The type name for this block.
     */
    @ExternalName
    public static final String TYPE = "list";

    @Override
    public Map<String, String> getMeta() {
        Map<String, String> out = new LinkedHashMap<String, String>();
        out.put(TYPE_META, TYPE);
        out.put(SIZE_META, Integer.toString(size));
        return out;
    }

    /**
     * @return how many keys are in the list.
     */
    public int count() {
        return entries.size();
    }

    /**
     * get a key from the list.
     * 
     * @param index
     *            the index into the list.
     * @return the key.
     */
    public FullKey get(int index) {
        return entries.get(index);
    }
}
