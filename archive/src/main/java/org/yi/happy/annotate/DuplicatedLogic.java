package org.yi.happy.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * places where there is a chunk of duplicated logic that can be extracted..
 */
@Retention(RetentionPolicy.SOURCE)
public @interface DuplicatedLogic {
    /**
     * @return what the duplication is.
     */
    String value();
}
