package org.yi.happy.archive.commandLine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describe the non-option arguments a command takes.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UsesArgs {

    /**
     * @return the names of the non-option arguments.
     */
    String[] value();
}
