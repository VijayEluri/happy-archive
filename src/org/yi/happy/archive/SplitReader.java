package org.yi.happy.archive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.DataBlock;
import org.yi.happy.archive.block.MapBlock;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.KeyParse;

/**
 * fetches the blocks for a split key of various kinds.
 */
public class SplitReader {

    private int progress;

    /**
     * create to read fullKey finding blocks in storage
     * 
     * @param fullKey
     *            the key to read
     * @param storage
     *            where to find the blocks
     */
    public SplitReader(FullKey fullKey, RetrieveBlock storage) {
	this.pending = new ArrayList<Pending>();
	this.pending.add(new Pending(fullKey, 0l));
	this.storage = storage;
    }

    /**
     * fetch any data block that is ready.
     * 
     * @return the offset to the data and the clear data, or null if none is
     *         ready.
     * @throws IOException
     *             if the block is available but fetching or decoding failed.
     */
    public Fragment fetchAny() throws IOException {
	for (int i = 0; i < pending.size(); i++) {
	    Fragment out = fetch(i);
	    if (out != null) {
		return out;
	    }
	}
	return null;
    }

    /**
     * get the first data block.
     * 
     * @return the offset and the clear data, or null if none is ready.
     * @throws IOException
     *             if the block is available but fetching or decoding failed.
     */
    public Fragment fetchFirst() throws IOException {
	return fetch(0);
    }

    /**
     * try to fetch the given entry in the pending list, if it can be fetched
     * return the details and remove it from the pending list. if the block
     * being fetched is an indirection then the pending list gets updated and
     * the attempt is repeated. If the offset of the entry in the pending list
     * is not known, it can not be fetched and null is returned.
     * 
     * @param index
     *            the index in the pending list to try and load
     * @return the loaded data for the given entry, or null if the block is not
     *         available.
     * @throws IOException
     *             if the block is available but fetching or decoding failed.
     */
    @MagicLiteral
    private Fragment fetch(int index) throws IOException {
	while (true) {
	    if (index >= pending.size()) {
		return null;
	    }

	    Pending item = pending.get(index);
	    if (item.offset == null) {
		return null;
	    }

	    Block b = storage.retrieveBlock(item.key);
	    if (b == null) {
		return null;
	    }

	    /*
	     * note that progress was made
	     */
	    progress++;

	    String type = b.getMeta().get("type");
	    if (type == null) {
		return processData(index, item.offset, b);
	    }

	    if (type.equals(MapBlock.TYPE)) {
		processMap(index, item.offset, b);
		continue;
	    }

	    if (type.equals("split")) {
		processSplit(index, item, b);
		continue;
	    }

	    if (type.equals("indirect")) {
		item.key = KeyParse.parseFullKey(ByteString.toString(b
			.getBody()));
		continue;
	    }

	    if (type.equals("list")) {
		processList(index, item.offset, b);
		continue;
	    }

	    throw new DecodeException("can not handle type: " + type);
	}
    }

    private Fragment processData(int index, long offset, Block b)
	    throws DecodeException {
	/*
	 * data block
	 */
	DataBlock block;
	try {
	    block = DataBlockParse.parse(b);
	} catch (IllegalArgumentException e) {
	    throw new DecodeException(e);
	}
	Bytes data = block.getBody();

	pending.remove(index);
	fixOffset(index, offset + data.getSize());
	return new Fragment(offset, data);
    }

    /**
     * replace an item with the contents of a split block
     * 
     * @param index
     *            the index of the item
     * @param item
     *            the item
     * @param b
     *            the split block
     */
    @MagicLiteral
    private void processSplit(int index, Pending item, Block b) {
	String countString = b.getMeta().get("split-count");
	int count = Integer.parseInt(countString);
	List<Pending> add = new ArrayList<Pending>(count);
	String base = item.key + "/";
	for (int i = 0; i < count; i++) {
	    FullKey key = KeyParse.parseFullKey(base + i);
	    add.add(new Pending(key, null));
	}

	replaceItem(index, add, item.offset);
    }

    /**
     * replace an item with a list of items, and fix the offset if needed
     * 
     * @param index
     *            the index of the item to replace
     * @param add
     *            what to replace the item with
     * @param offset
     *            the offset of the first block if missing
     */
    private void replaceItem(int index, List<Pending> add, long offset) {
	pending.remove(index);
	pending.addAll(index, add);
	fixOffset(index, offset);
    }

    /**
     * replace the pending block at index with the contents of the map block
     * 
     * @param index
     *            the index to replace
     * @param base
     *            the base offset of the block, for chainging relative offsets
     *            into absolute offsets.
     * @param b
     *            a map block
     */
    @MagicLiteral
    private void processMap(int index, long base, Block b) {
	String map = ByteString.toString(b.getBody().toByteArray());
	String[] lines = map.split("\n");
	List<Pending> add = new ArrayList<Pending>(lines.length);
	for (String line : lines) {
	    String[] cols = line.split("\t", 2);
	    FullKey key = KeyParse.parseFullKey(cols[0]);
	    long offset = Long.parseLong(cols[1]) + base;
	    add.add(new Pending(key, offset));
	}

	replaceItem(index, add, base);
    }

    /**
     * replace an item with the contents of a list block.
     * 
     * @param index
     *            the index to the item
     * @param base
     *            the base offset of the item
     * @param b
     *            the block
     */
    @MagicLiteral
    private void processList(int index, long base, Block b) {
	String map = ByteString.toString(b.getBody());
	String[] lines = map.split("\n");
	List<Pending> add = new ArrayList<Pending>(lines.length);
	for (String line : lines) {
	    FullKey key = KeyParse.parseFullKey(line);
	    add.add(new Pending(key, null));
	}

	replaceItem(index, add, base);
    }

    /**
     * fix the offset of an item. the offset of the item is set to offset if it
     * is not already set.
     * 
     * @param index
     *            the index of the item
     * @param offset
     *            the offset to set if it is not already set
     */
    private void fixOffset(int index, long offset) {
	if (pending.size() <= index) {
	    return;
	}

	Pending item = pending.get(index);
	if (item.offset != null) {
	    return;
	}

	item.offset = offset;
    }

    /**
     * get the list of blocks that are needed at this time. a later call to this
     * may return more blocks in the list.
     * 
     * @return the list full keys for blocks that are needed.
     */
    public List<FullKey> getPending() {
	List<FullKey> out = new ArrayList<FullKey>(pending.size());
	for (Pending i : pending) {
	    out.add(i.key);
	}
	return out;
    }

    /**
     * is the reader out of blocks to read
     * 
     * @return true if there are no more pending blocks.
     */
    public boolean isDone() {
	return pending.isEmpty();
    }

    /**
     * trivial data structure for pending blocks
     * 
     * @author sarah dot a dot happy at gmail dot com
     * 
     */
    private static class Pending {
	/**
	 * create an entry
	 * 
	 * @param key
	 *            the key value
	 * @param offset
	 *            the offset value
	 */
	public Pending(FullKey key, Long offset) {
	    this.key = key;
	    this.offset = offset;
	}

	/**
	 * the byte offset from the creating key where this block should appear
	 */
	public Long offset;

	/**
	 * the key that needs to be loaded in this position
	 */
	public FullKey key;
    }

    private List<Pending> pending;

    private RetrieveBlock storage;

    /**
     * get the offset where the first entry in the pending list will go
     * 
     * @return the next offset, or null if done
     */
    public Long getOffset() {
	if (pending.isEmpty()) {
	    return null;
	}
	return pending.get(0).offset;
    }

    /**
     * get the progress counter. At this time the return value of this is
     * different if any progress was made reading blocks since the last time
     * this was called. The initial value is zero.
     * 
     * @return the progress counter, which at the moment is the number of blocks
     *         successfully retrieved from storage.
     */
    public int getProgress() {
	return progress;
    }
}
