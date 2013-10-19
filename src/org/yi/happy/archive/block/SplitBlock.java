package org.yi.happy.archive.block;

import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.annotate.ExternalName;
import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.Bytes;

/**
 * The first attempt at splitting up large files into chunks. The split block is
 * stored under a name key, and each block is stored under the name key (name
 * slash number).
 */
public class SplitBlock extends AbstractBlock {

    private final int count;

    /**
     * create the split block with a count.
     * 
     * @param count
     *            now many parts the data is split into.
     */
    public SplitBlock(int count) {
        this.count = count;
    }

    /**
     * the meta-data field name for the type.
     */
    @ExternalName
    public static final String TYPE_META = "type";

    /**
     * the type name for this block.
     */
    @ExternalName
    public static final String TYPE = "split";

    /**
     * the meta-data field name for the split count.
     */
    @ExternalName
    public static final String SPLIT_COUNT_META = "split-count";

    @MagicLiteral
    @Override
    public Map<String, String> getMeta() {
        Map<String, String> out = new LinkedHashMap<String, String>();
        out.put(TYPE_META, TYPE);
        out.put(SPLIT_COUNT_META, Integer.toString(count));
        out.put(SIZE_META, "0");
        return out;
    }

    @Override
    public Bytes getBody() {
        return new Bytes();
    }

    /**
     * @return the number of parts the data is split into.
     */
    public int getCount() {
        return count;
    }

}
