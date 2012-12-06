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
