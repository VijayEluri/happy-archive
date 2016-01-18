package org.yi.happy.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Notes a method that violates an interface contract.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface BrokenContract {
    /**
     * @return the interface that declares the contract that is being violated.
     */
    Class<?> value();
}
