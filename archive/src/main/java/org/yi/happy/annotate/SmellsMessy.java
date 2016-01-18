package org.yi.happy.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Things that look or smell messy. There are probably several cleanups that can
 * be done to make the code clearer.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface SmellsMessy {

}
