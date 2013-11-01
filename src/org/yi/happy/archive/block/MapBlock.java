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
        /**
         * the key field.
         */
        private final FullKey key;

        /**
         * the offset field.
         */
        private final long offset;

        /**
         * the size of this entry in bytes.
         */
        private final int entrySize;

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

            byte[] k = ByteString.toUtf8(key.toString());
            byte[] o = ByteString.toUtf8(Long.toString(offset));
            entrySize = k.length + 1 + o.length + 1;
        }

        /**
         * @return the size of this entry in bytes.
         */
        public int getEntrySize() {
            return entrySize;
        }

        /**
         * copy all the bytes from this entry.
         * 
         * @param dest
         *            the destination byte array.
         * @param destPos
         *            the position in the destination byte array to copy to.
         * @return the number of bytes copied (the size of this entry in bytes).
         */
        public int getBytes(byte[] dest, int destPos) {
            byte[] src;

            src = ByteString.toUtf8(key.toString());
            System.arraycopy(src, 0, dest, destPos, src.length);
            destPos += src.length;

            dest[destPos] = FIELD_SEPARATOR;
            destPos++;

            src = ByteString.toUtf8(Long.toString(offset));
            System.arraycopy(src, 0, dest, destPos, src.length);
            destPos += src.length;

            dest[destPos] = RECORD_SEPARATOR;
            destPos++;

            return entrySize;
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
        for (Entry entry : entries) {
            size += entry.getEntrySize();
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
        int pos = 0;
        for (Entry entry : entries) {
            pos += entry.getBytes(out, pos);
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

    /**
     * The number of entries in this block.
     * 
     * @return the number of entries in this block.
     */
    public int count() {
        return entries.size();
    }

    /**
     * get the key from an entry in this block.
     * 
     * @param index
     *            the index of the entry.
     * @return the key.
     */
    public FullKey getKey(int index) {
        return entries.get(index).getKey();
    }

    /**
     * get the offset from an entry in this block.
     * 
     * @param index
     *            the index of the entry.
     * @return the offset.
     */
    public long getOffset(int index) {
        return entries.get(index).getOffset();
    }

}
