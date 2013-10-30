package org.yi.happy.archive;

import java.io.IOException;
import java.util.Iterator;

import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.key.LocatorKey;

/**
 * A block storage service.
 */
public interface BlockStore extends Iterable<LocatorKey> {
    /**
     * put a block into the store.
     * 
     * @param block
     *            the block to put into the store.
     * @throws IOException
     *             if an error occurred putting the block in the store.
     */
    void put(EncodedBlock block) throws IOException;

    /**
     * get a block from the store.
     * 
     * @param key
     *            the key to fetch.
     * @return the block from the store, or null if the block is not available.
     * @throws IOException
     *             on errors other than the block not being available.
     */
    EncodedBlock get(LocatorKey key) throws IOException;

    /**
     * Visit all the keys in the store.
     * 
     * @param <T>
     *            the type of the exceptions that can be thrown from the
     *            visitor.
     * 
     * @param visitor
     *            the visitor that is visiting the keys in the store.
     * @throws T
     */
    <T extends Throwable> void visit(BlockStoreVisitor<T> visitor) throws T;

    /**
     * Test if the store appears to contain the given key.
     * 
     * @param key
     *            the key to test for.
     * @return true if the store appears to contain key.
     * @throws IOException
     */
    boolean contains(LocatorKey key) throws IOException;

    /**
     * Remove a key from the store.
     * 
     * @param key
     *            the key to remove.
     * @throws IOException
     *             on error.
     */
    void remove(LocatorKey key) throws IOException;

    /**
     * get the creation time of a key in the store.
     * 
     * @param key
     * @return the creation time.
     * @throws IOException
     */
    long getTime(LocatorKey key) throws IOException;

    /**
     * Iterate over all the keys in the store. The keys will be returned in
     * default order.
     * 
     * @return the {@link Iterator}.
     */
    @Override
    public Iterator<LocatorKey> iterator();
}
