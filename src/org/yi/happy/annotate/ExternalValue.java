package org.yi.happy.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks values and sequences that exist external to the system and can not be
 * changed without breaking support for legacy data.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface ExternalValue {
    /**
     * the choices for types of external names.
     */
    public enum Type {
        /**
         * the value of the constant is the external value.
         */
        VALUE;
    }

    /**
     * @return the type of external value that this is referring to.
     */
    public Type value() default Type.VALUE;
}
