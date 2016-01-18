package org.yi.happy.archive.test_data;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the full version two key of a data block.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@interface Full {
    /**
     * the full key value given
     * 
     * @return the full key
     */
    String value();

}
