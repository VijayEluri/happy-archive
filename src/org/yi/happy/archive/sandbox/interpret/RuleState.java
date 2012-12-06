package org.yi.happy.archive.sandbox.interpret;

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
    public RuleState startStream(ActionCallback callback) {
        Rule rule = null;
        for (Rule r : rules) {
            if (r.getOn().startStream()) {
                rule = r;
                break;
            }
        }
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        rule.getAction().startStream(callback);
        return rule.getGo();
    }

    @Override
    public RuleState endStream(ActionCallback callback) {
        Rule rule = null;
        for (Rule r : rules) {
            if (r.getOn().endStream()) {
                rule = r;
                break;
            }
        }
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        rule.getAction().endStream(callback);
        return rule.getGo();
    }

    @Override
    public RuleState data(byte b, ActionCallback callback) {
        Rule rule = null;
        for (Rule r : rules) {
            if (r.getOn().data(b)) {
                rule = r;
                break;
            }
        }
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        rule.getAction().data(callback, b);
        return rule.getGo();
    }

    @Override
    public RuleState startRegion(String name, ActionCallback callback) {
        Rule rule = null;
        for (Rule r : rules) {
            if (r.getOn().startRegion(name)) {
                rule = r;
                break;
            }
        }
        if (rule == null) {
            throw new UnsupportedOperationException();
        }

        rule.getAction().startRegion(callback, name);

        return rule.getGo();
    }

    @Override
    public RuleState endRegion(String name, ActionCallback callback) {
        Rule rule = null;
        for (Rule r : rules) {
            if (r.getOn().endRegion(name)) {
                rule = r;
                break;
            }
        }
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        rule.getAction().endRegion(callback, name);
        return rule.getGo();
    }

}
