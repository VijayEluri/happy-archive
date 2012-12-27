package org.yi.happy.archive.restore;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple map builder. This builder makes {@link LinkedHashMap}s so that the
 * keys are in the same order as they were added.
 * 
 * @param <Key>
 *            the type of the key.
 * @param <Value>
 *            the type of the value.
 */
public class MapBuilder<Key, Value> {
    private LinkedHashMap<Key, Value> map;

    /**
     * create a simple map builder.
     */
    public MapBuilder() {
        map = new LinkedHashMap<Key, Value>();
    }

    /**
     * Add an entry to the map. If two values are added with the same key they
     * value is replaced.
     * 
     * @param key
     *            the key to add.
     * @param value
     *            the value to add.
     * @return this.
     * @see Map#put(Object, Object)
     */
    public MapBuilder<Key, Value> add(Key key, Value value) {
        map.put(key, value);
        return this;
    }

    /**
     * Create the built map.
     * 
     * @return A copy of the map in this builder.
     */
    public Map<Key, Value> create() {
        return new LinkedHashMap<Key, Value>(map);
    }
}
