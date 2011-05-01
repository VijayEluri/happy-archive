package org.yi.happy.archive.tag;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A builder for {@link Tag} objects.
 */
public class TagBuilder {
    private final class TagBuilderAdd extends TagBuilder {
        private final String key;
        private final String value;

        private TagBuilderAdd(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Map<String, String> createMap() {
            Map<String, String> map = TagBuilder.this.createMap();
            if (!map.containsKey(key)) {
                map.put(key, value);
            }
            return map;
        }
    }

    private final class TagBuilderPut extends TagBuilder {
        private final String key;
        private final String value;

        private TagBuilderPut(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Map<String, String> createMap() {
            Map<String, String> map = TagBuilder.this.createMap();
            map.put(key, value);
            return map;
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
    public TagBuilder add(final String key, final String value) {
        return new TagBuilderAdd(key, value);
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
    public TagBuilder put(final String key, final String value) {
        return new TagBuilderPut(key, value);
    }

    /**
     * Create the tag.
     * 
     * @return the specified tag.
     */
    public Tag create() {
        return new Tag(createMap());
    }

    /**
     * Create the map for the tag.
     * 
     * @return the map for the tag.
     */
    public Map<String, String> createMap() {
        return new LinkedHashMap<String, String>();
    }
}
