package org.yi.happy.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * notes a method or class that uses the global file system interface. This
 * makes it harder to test because of the dependence on shared global resources.
 * Mostly this means that unit tests of noted logic are hard to run in parallel.
 * Ideally this note infects methods that directly refer to classes and methods
 * that use the global file system interface.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface GlobalFilesystem {

}
