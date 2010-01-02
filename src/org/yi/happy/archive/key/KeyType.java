package org.yi.happy.archive.key;

import org.yi.happy.annotate.ExternalName;

/**
 * the key types.
 */
public class KeyType {
    private KeyType() {
	// nothing
    }

    /**
     * the type name for a content type key
     */
    @ExternalName
    public static final String CONTENT_HASH = "content-hash";

    /**
     * the type name for a name type key
     */
    @ExternalName
    public static final String NAME_HASH = "name-hash";

    /**
     * the type name for a blob type key
     */
    public static final String BLOB = "blob";

}
