package org.yi.happy.archive.tag;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A file tag, these are the meta-data for files and directories in the archive.
 */
public class Tag {

    private final Map<String, String> fields;

    /**
     * create.
     * 
     * @param fields
     *            the fields.
     */
    public Tag(Map<String, String> fields) {
        this.fields = new LinkedHashMap<String, String>(fields);
    }

    /**
     * get a field.
     * 
     * @param field
     *            the field to get.
     * @return the value of that field, or null if not found.
     */
    public String get(String field) {
        return fields.get(field);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fields.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tag other = (Tag) obj;
        if (!fields.equals(other.fields))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Tag [fields=" + fields + "]";
    }
}
