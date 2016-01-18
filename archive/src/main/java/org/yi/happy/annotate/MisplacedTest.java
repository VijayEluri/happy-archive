package org.yi.happy.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A test that is in the wrong test suite. Misplaced tests are easily identified
 * by not calling out to the class under test in the exercise phase.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface MisplacedTest {
    /**
     * Where this test probably belongs.
     * 
     * @return where this test probably belongs.
     */
    Class<?>[] value() default {};
}
