package org.yi.happy.archive.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.key.FullKey;

public class MapBlock extends AbstractBlock implements Block {
    public static class Entry {
	private final FullKey key;
	private final long offset;

	public FullKey getKey() {
	    return key;
	}

	public long getOffset() {
	    return offset;
	}

	public Entry(FullKey key, long offset) {
	    super();
	    this.key = key;
	    this.offset = offset;
	}
    }

    private final List<Entry> entries;

    private final int size;
    
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

    @Override
    @MagicLiteral
    public Bytes getBody() {
	byte[] out = new byte[size];
	int i = 0;
	for (Entry j : entries) {
	    byte[] k = ByteString.toUtf8(j.getKey().toString());
	    byte[] o = ByteString.toUtf8("" + j.getOffset());

	    System.arraycopy(k, 0, out, i, k.length);
	    i += k.length;
	    out[i] = '\t';
	    i++;
	    System.arraycopy(o, 0, out, i, o.length);
	    i += o.length;
	    out[i] = '\n';
	    i++;
	}
	return new Bytes(out);
    }

    @Override
    @MagicLiteral
    public Map<String, String> getMeta() {
	Map<String, String> out = new LinkedHashMap<String, String>();
	out.put("type", "map");
	out.put("size", "" + size);
	return out;
    }

}
