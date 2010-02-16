package org.yi.happy.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks names that exist external to the system and can not be changed without
 * breaking support for legacy data.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface ExternalName {
    /**
     * the choices for types of external names.
     */
    public enum Type {
	/**
	 * the value of the constant is the external name.
	 */
	VALUE;
    }

    /**
     * @return the type of external name that this is referring to.
     */
    public Type value() default Type.VALUE;
}
