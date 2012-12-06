package org.yi.happy.archive.sandbox.interpret;

import java.util.IdentityHashMap;
import java.util.List;

public class RuleState {
    public Rules<RuleState> rules = new Rules<RuleState>();

    public static <Type> RuleState compile(List<Rule<Type>> rules, Type first) {
        IdentityHashMap<Object, RuleState> index = new IdentityHashMap<Object, RuleState>();
        for (Rule<Type> rule : rules) {
            RuleState in = index.get(rule.getIn());
            if (in == null) {
                in = new RuleState();
                index.put(rule.getIn(), in);
            }
            RuleState go = index.get(rule.getGo());
            if (go == null) {
                go = new RuleState();
                index.put(rule.getGo(), go);
            }

            in.add(new Rule<RuleState>(in, rule.getOn(), rule.getAction(), go));
        }
        return index.get(first);
    }

    public static <Type> RuleState compile(Rules<Type> rules) {
        return compile(rules.getRules(), rules.getStartState());
    }

    private void add(Rule<RuleState> rule) {
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
}
