package org.yi.happy.archive.restore;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapBuilder<Key, Value> {
    private LinkedHashMap<Key, Value> map;

    public MapBuilder() {
        map = new LinkedHashMap<Key, Value>();
    }

    public MapBuilder<Key, Value> add(Key key, Value value) {
        map.put(key, value);
        return this;
    }

    public Map<Key, Value> create() {
        return new LinkedHashMap<Key, Value>(map);
    }
}
