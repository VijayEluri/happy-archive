package org.yi.happy.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Places where there is a switch like structure based on types. Consider
 * re-working these into instances of the visitor pattern.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeSwitch {

}
