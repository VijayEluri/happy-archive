package org.yi.happy.archive;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class Sets {

    public static <T> Set<T> asSet(T... elements) {
	return new LinkedHashSet<T>(Arrays.asList(elements));
    }

}
