package org.yi.happy.archive;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Utility methods for {@link Set}s.
 */
public class Sets {

    /**
     * create a set from a list of arguments.
     * 
     * @param <T>
     *            the type of the arguments.
     * @param elements
     *            the elements of the set.
     * @return a linked hash set of the given elements.
     */
    public static <T> Set<T> asSet(T... elements) {
        return new LinkedHashSet<T>(Arrays.asList(elements));
    }

}
