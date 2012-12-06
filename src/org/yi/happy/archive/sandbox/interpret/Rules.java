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
     * find the rule matching state and startStream.
     * 
     * @param state
     *            the state to match.
     * @return the matching rule or null if not found.
     */
    public Rule<Type> startStream(Type state) {
        for (Rule<Type> rule : rules) {
            if (rule.getIn() == state && rule.getOn().startStream()) {
                return rule;
            }
        }
        return null;
    }

    /**
     * find the rule matching state and endStream.
     * 
     * @param state
     *            the state to match.
     * @return the matching rule or null if not found.
     */
    public Rule<Type> endStream(Type state) {
        for (Rule<Type> rule : rules) {
            if (rule.getIn() == state && rule.getOn().endStream()) {
                return rule;
            }
        }
        return null;
    }

    /**
     * find the rule matching state and data.
     * 
     * @param state
     *            the state to match.
     * @param b
     *            the data to use in the match.
     * @return the matching rule or null if not found.
     */
    public Rule<Type> data(Type state, byte b) {
        for (Rule<Type> rule : rules) {
            if (rule.getIn() == state && rule.getOn().data(b)) {
                return rule;
            }
        }
        return null;
    }

    /**
     * find the rule matching state and start of a region.
     * 
     * @param state
     *            the state to match.
     * @param name
     *            the region name to use in the match.
     * @return the matching rule or null if not found.
     */
    public Rule<Type> startRegion(Type state, String name) {
        for (Rule<Type> rule : rules) {
            if (rule.getIn() == state && rule.getOn().startRegion(name)) {
                return rule;
            }
        }
        return null;
    }

    /**
     * find the rule matching state and end of a region.
     * 
     * @param state
     *            the state to match.
     * @param name
     *            the region name to use in the match.
     * @return the matching rule or null if not found.
     */
    public Rule<Type> endRegion(Type state, String name) {
        for (Rule<Type> rule : rules) {
            if (rule.getIn() == state && rule.getOn().endRegion(name)) {
                return rule;
            }
        }
        return null;
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
