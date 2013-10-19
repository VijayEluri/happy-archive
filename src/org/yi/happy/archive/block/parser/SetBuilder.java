package org.yi.happy.archive.block.parser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A builder for {@link Set} objects.
 * 
 * @param <Type>
 *            the type of the elements in the set.
 */
public class SetBuilder<Type> {
    private HashSet<Type> set;

    /**
     * create blank.
     */
    public SetBuilder() {
        set = new HashSet<Type>();
    }

    /**
     * create with values.
     * 
     * @param values
     *            the values.
     */
    public SetBuilder(Type... values) {
        this();
        for (Type value : values) {
            set.add(value);
        }
    }

    /**
     * add values.
     * 
     * @param values
     *            the values.
     * @return this
     */
    public SetBuilder<Type> add(Type... values) {
        for (Type value : values) {
            set.add(value);
        }
        return this;
    }

    /**
     * @return an immutable set from the state of this builder.
     */
    public Set<Type> createImmutable() {
        return Collections.unmodifiableSet(new HashSet<Type>(set));
    }
}
