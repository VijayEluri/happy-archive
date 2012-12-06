package org.yi.happy.archive.sandbox.interpret;

import java.util.ArrayList;
import java.util.List;

public class RuleState {
    public List<Rule<RuleState>> rules = new ArrayList<Rule<RuleState>>();

    public void add(Rule<RuleState> rule) {
        rules.add(rule);
    }

    public Rule<RuleState> startStream() {
        Rule<RuleState> rule = null;
        for (Rule<RuleState> r : rules) {
            if (r.getOn().startStream()) {
                rule = r;
                break;
            }
        }
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        return rule;
    }

    public Rule<RuleState> endStream() {
        Rule<RuleState> rule = null;
        for (Rule<RuleState> r : rules) {
            if (r.getOn().endStream()) {
                rule = r;
                break;
            }
        }
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        return rule;
    }

    public Rule<RuleState> data(byte b) {
        Rule<RuleState> rule = null;
        for (Rule<RuleState> r : rules) {
            if (r.getOn().data(b)) {
                rule = r;
                break;
            }
        }
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        return rule;
    }

    public Rule<RuleState> startRegion(String name) {
        Rule<RuleState> rule = null;
        for (Rule<RuleState> r : rules) {
            if (r.getOn().startRegion(name)) {
                rule = r;
                break;
            }
        }
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        return rule;
    }

    public Rule<RuleState> endRegion(String name) {
        Rule<RuleState> rule = null;
        for (Rule<RuleState> r : rules) {
            if (r.getOn().endRegion(name)) {
                rule = r;
                break;
            }
        }
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        return rule;
    }

    public void add(OnCondition onCondition, DoAction doAction,
            RuleState goState) {
        add(new Rule<RuleState>(onCondition, doAction, goState));
    }
}
