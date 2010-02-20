package org.yi.happy.archive.test_data;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the locator version two key of a data block.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@interface Locator {
    /**
     * the locator key value given
     * 
     * @return the locator key
     */
    String value();

}
