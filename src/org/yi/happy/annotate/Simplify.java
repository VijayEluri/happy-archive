package org.yi.happy.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * places where things can be simplified while becoming more useful or powerful.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Simplify {
    /**
     * @return what the simplification is.
     */
    String value();
}
