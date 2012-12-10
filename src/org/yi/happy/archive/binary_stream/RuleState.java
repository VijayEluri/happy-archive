package org.yi.happy.archive.binary_stream;

import java.util.ArrayList;
import java.util.List;

/**
 * A rule driven state in an interpreter based parsing state machine.
 */
public class RuleState implements State {
    private List<Rule> rules = new ArrayList<Rule>();

    /**
     * Add a rule to the set of rules in this state.
     * 
     * @param rule
     *            the rule to add.
     */
    public void add(Rule rule) {
        rules.add(rule);
    }

    /**
     * Add a rule to the set of rules in this state.
     * 
     * @param onCondition
     *            the condition for the rule.
     * @param doAction
     *            the action for the rule.
     * @param goState
     *            the next state for the rule.
     */
    public void add(OnCondition onCondition, DoAction doAction,
            RuleState goState) {
        add(new Rule(onCondition, doAction, goState));
    }

    @Override
    public State startStream(ActionCallback callback) {
        for (Rule rule : rules) {
            if (rule.getOn().startStream()) {
                rule.getAction().startStream(callback);
                return rule.getGo();
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public State endStream(ActionCallback callback) {
        for (Rule rule : rules) {
            if (rule.getOn().endStream()) {
                rule.getAction().endStream(callback);
                return rule.getGo();
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public State data(byte b, ActionCallback callback) {
        for (Rule rule : rules) {
            if (rule.getOn().data(b)) {
                rule.getAction().data(callback, b);
                return rule.getGo();
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public State startRegion(String name, ActionCallback callback) {
        for (Rule rule : rules) {
            if (rule.getOn().startRegion(name)) {
                rule.getAction().startRegion(callback, name);
                return rule.getGo();
            }
        }
        throw new UnsupportedOperationException();

    }

    @Override
    public State endRegion(String name, ActionCallback callback) {
        for (Rule rule : rules) {
            if (rule.getOn().endRegion(name)) {
                rule.getAction().endRegion(callback, name);
                return rule.getGo();
            }
        }
        throw new UnsupportedOperationException();
    }

}
