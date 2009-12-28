package org.yi.happy.archive.key;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks names that exist external to the system and can not be changed without
 * breaking support for legacy data.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface ExternalName {
    public enum Type {
        /**
         * the value of the constant is the external name.
         */
        VALUE;
    }

    public Type value() default Type.VALUE;
}
