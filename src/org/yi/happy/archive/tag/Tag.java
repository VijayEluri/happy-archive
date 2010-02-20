package org.yi.happy.archive.tag;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A file tag, these are the meta-data for files and directories in the archive.
 */
public class Tag {

    private Map<String, String> fields;

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

}
