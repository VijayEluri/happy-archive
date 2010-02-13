package org.yi.happy.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * marks where magic literals are in use. One way to fix them is to extract them
 * into declared constants.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface MagicLiteral {

}
