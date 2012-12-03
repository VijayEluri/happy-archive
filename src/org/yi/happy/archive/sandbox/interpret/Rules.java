package org.yi.happy.archive.sandbox.interpret;

import java.util.ArrayList;
import java.util.List;

/**
 * Finite state machine rules for {@link InterpretFilter}.
 */
public class Rules {

    private List<Rule> rules = new ArrayList<Rule>();
    private String startState;

    /**
     * Add a rule to the set of rules.
     * 
     * @param rule
     *            the rule to add.
     */
    public void add(Rule rule) {
        rules.add(rule);
    }

    /**
     * find the rule matching state and startStream.
     * 
     * @param state
     *            the state to match.
     * @return the matching rule or null if not found.
     */
    public Rule startStream(Object state) {
        for (Rule rule : rules) {
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
    public Rule endStream(Object state) {
        for (Rule rule : rules) {
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
    public Rule data(Object state, byte b) {
        for (Rule rule : rules) {
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
    public Rule startRegion(Object state, String name) {
        for (Rule rule : rules) {
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
    public Rule endRegion(Object state, String name) {
        for (Rule rule : rules) {
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
    public void setStartState(String startState) {
        this.startState = startState;
    }

    /**
     * get the starting state.
     * 
     * @return the starting state.
     */
    public String getStartState() {
        return startState;
    }
}
