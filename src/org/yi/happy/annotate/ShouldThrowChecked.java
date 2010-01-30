package org.yi.happy.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Things that should throw checked exceptions. These are things that reach
 * outside of the program where things could fail without the programmer being
 * at fault.
 */
@Retention(RetentionPolicy.SOURCE)
@Target( { ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface ShouldThrowChecked {

}
