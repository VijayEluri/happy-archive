package org.yi.happy.archive.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yi.happy.annotate.ExternalName;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.key.FullKey;

/**
 * A block of keys and offsets where the keys apply to. This was created to make
 * restores faster when the keys were available out of order. This was made as a
 * replacement for the list type block.
 */
public class MapBlock extends AbstractBlock implements Block {
    /**
     * An entry in the block.
     */
    public static class Entry {
        private final FullKey key;
        private final long offset;

        /**
         * get the key part of the entry.
         * 
         * @return the key part of the entry.
         */
        public FullKey getKey() {
            return key;
        }

        /**
         * get the offset part of the entry.
         * 
         * @return the offset where the entry applies, relative to the start of
         *         the block.
         */
        public long getOffset() {
            return offset;
        }

        /**
         * create a map entry.
         * 
         * @param key
         *            the key in the map.
         * @param offset
         *            the offset where the entry applies, relative to the start
         *            of the block.
         */
        public Entry(FullKey key, long offset) {
            super();
            this.key = key;
            this.offset = offset;
        }
    }

    private final List<Entry> entries;

    private final int size;

    /**
     * create a map block from a list of map block entries.
     * 
     * @param entries
     *            the map block entries.
     */
    public MapBlock(List<Entry> entries) {
        entries = new ArrayList<Entry>(entries);
        this.entries = Collections.unmodifiableList(entries);

        int size = 0;
        for (Entry i : entries) {
            byte[] k = ByteString.toUtf8(i.getKey().toString());
            byte[] o = ByteString.toUtf8("" + i.getOffset());
            size += k.length + 1 + o.length + 1;
        }
        this.size = size;
    }

    /**
     * the byte that separates fields in each entry record.
     */
    public static final byte FIELD_SEPARATOR = '\t';

    /**
     * the byte that separates records in the block.
     */
    public static final byte RECORD_SEPARATOR = '\n';

    @Override
    public Bytes getBody() {
        byte[] out = new byte[size];
        int i = 0;
        for (Entry j : entries) {
            byte[] k = ByteString.toUtf8(j.getKey().toString());
            byte[] o = ByteString.toUtf8(Long.toString(j.getOffset()));

            System.arraycopy(k, 0, out, i, k.length);
            i += k.length;
            out[i] = FIELD_SEPARATOR;
            i++;
            System.arraycopy(o, 0, out, i, o.length);
            i += o.length;
            out[i] = RECORD_SEPARATOR;
            i++;
        }
        return new Bytes(out);
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
    public static final String TYPE = "map";

    @Override
    public Map<String, String> getMeta() {
        Map<String, String> out = new LinkedHashMap<String, String>();
        out.put(TYPE_META, TYPE);
        out.put(SIZE_META, Integer.toString(size));
        return out;
    }

    public int count() {
        return entries.size();
    }

    public FullKey getKey(int index) {
        return entries.get(index).getKey();
    }

    public long getOffset(int index) {
        return entries.get(index).getOffset();
    }

}
