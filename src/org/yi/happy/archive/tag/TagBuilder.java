package org.yi.happy.archive.tag;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A builder for {@link Tag} objects.
 */
public final class TagBuilder {
    private final Map<String, String> map;

    /**
     * Start with blank.
     */
    public TagBuilder() {
        this(null);
    }

    /**
     * Start with a value.
     * 
     * @param tag
     *            the initial value.
     */
    public TagBuilder(Tag tag) {
        this.map = new LinkedHashMap<String, String>();

        if (tag == null) {
            return;
        }

        for (String field : tag.getFields()) {
            map.put(field, tag.get(field));
        }
    }

    /**
     * Add a field to the tag being built. If the field is already in the tag it
     * is ignored.
     * 
     * @param key
     *            the name of the field.
     * @param value
     *            the value of the field.
     * @return a builder that adds the given field.
     */
    public final TagBuilder add(String key, String value) {
        if (key.contains("=")) {
            throw new IllegalArgumentException();
        }
        if (!map.containsKey(key)) {
            map.put(key, value);
        }
        return this;
    }

    /**
     * Put a field into the tag being built. If the field is already in the tag
     * it is replaced.
     * 
     * @param key
     *            the name of the field.
     * @param value
     *            the value of the field.
     * @return a builder that stores the given field.
     */
    public final TagBuilder put(String key, String value) {
        if (key.contains("=")) {
            throw new IllegalArgumentException();
        }
        map.put(key, value);
        return this;
    }

    /**
     * Create the tag.
     * 
     * @return the specified tag.
     */
    public final Tag create() {
        return new Tag(map);
    }
}
