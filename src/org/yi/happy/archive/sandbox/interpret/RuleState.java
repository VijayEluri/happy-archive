package org.yi.happy.archive.sandbox.interpret;

import java.util.ArrayList;
import java.util.List;

public class RuleState {
    public List<Rule> rules = new ArrayList<Rule>();

    public void add(Rule rule) {
        rules.add(rule);
    }

    public void add(OnCondition onCondition, DoAction doAction,
            RuleState goState) {
        add(new Rule(onCondition, doAction, goState));
    }

    public Rule startStream() {
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
        return rule;
    }

    public Rule endStream() {
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
        return rule;
    }

    public Rule data(byte b) {
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
        return rule;
    }

    public Rule startRegion(String name) {
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
        return rule;
    }

    public Rule endRegion(String name) {
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
        return rule;
    }

}
