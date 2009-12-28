package org.yi.happy.archive.test_data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * a reference to the clear text version of a block
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Clear {

    /**
     * the clear text version of the block
     * 
     * @return the value
     */
	TestData value();

}
