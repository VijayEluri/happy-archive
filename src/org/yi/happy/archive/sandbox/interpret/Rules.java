package org.yi.happy.archive.sandbox.interpret;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Finite state machine rules for {@link InterpretFilter}.
 * 
 * @param <Type>
 *            the type of the state points in the rule.
 */
public class Rules<Type> implements Iterable<Rule<Type>> {

    private List<Rule<Type>> rules = new ArrayList<Rule<Type>>();
    private Type startState;

    /**
     * Add a rule to the set of rules.
     * 
     * @param rule
     *            the rule to add.
     */
    public void add(Rule<Type> rule) {
        rules.add(rule);
    }

    /**
     * set the starting state.
     * 
     * @param startState
     *            the starting state.
     */
    public void setStartState(Type startState) {
        this.startState = startState;
    }

    /**
     * get the starting state.
     * 
     * @return the starting state.
     */
    public Type getStartState() {
        return startState;
    }

    public List<Rule<Type>> getRules() {
        return rules;
    }

    @Override
    public Iterator<Rule<Type>> iterator() {
        return rules.iterator();
    }
}
