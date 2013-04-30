package org.yi.happy.archive.block;

import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.Bytes;

public class SplitBlock extends AbstractBlock {

    private final int count;

    public SplitBlock(int count) {
        this.count = count;
    }

    @MagicLiteral
    @Override
    public Map<String, String> getMeta() {
        Map<String, String> out = new LinkedHashMap<String, String>();
        out.put("type", "split");
        out.put("split-count", Integer.toString(count));
        out.put("size", "0");
        return out;
    }

    @Override
    public Bytes getBody() {
        return new Bytes();
    }

    public int getCount() {
        return count;
    }

}
