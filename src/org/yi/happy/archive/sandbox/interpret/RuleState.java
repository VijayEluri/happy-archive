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

    public Rule<RuleState> startStream(RuleState state) {
        Rule<RuleState> rule = rules.startStream(state);
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        return rule;
    }

    public Rule<RuleState> endStream(RuleState state) {
        Rule<RuleState> rule = rules.endStream(state);
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        return rule;
    }

    public Rule<RuleState> data(RuleState state, byte b) {
        Rule<RuleState> rule = rules.data(state, b);
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        return rule;
    }

    public Rule<RuleState> startRegion(RuleState state, String name) {
        Rule<RuleState> rule = rules.startRegion(state, name);
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        return rule;
    }

    public Rule<RuleState> endRegion(RuleState state, String name) {
        Rule<RuleState> rule = rules.endRegion(state, name);
        if (rule == null) {
            throw new UnsupportedOperationException();
        }
        return rule;
    }
}
