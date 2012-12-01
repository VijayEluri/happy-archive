package org.yi.happy.archive.block.parser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetBuilder<Type> {
    private HashSet<Type> set;

    public SetBuilder() {
        set = new HashSet<Type>();
    }

    public SetBuilder(Type... values) {
        this();
        for (Type value : values) {
            set.add(value);
        }
    }

    public SetBuilder<Type> add(Type... values) {
        for (Type value : values) {
            set.add(value);
        }
        return this;
    }

    public Set<Type> createImmutable() {
        return Collections.unmodifiableSet(new HashSet<Type>(set));
    }
}
