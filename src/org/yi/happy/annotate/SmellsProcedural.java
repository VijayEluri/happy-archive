package org.yi.happy.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Classes that look like procedural libraries, but have non-static methods.
 * Classes that only have methods, no instance variables, and no extension
 * points.
 * 
 * Making all the methods in the class may be a good idea.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface SmellsProcedural {

}
