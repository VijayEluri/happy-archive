package org.yi.happy.archive.sandbox.interpret;

import java.util.ArrayList;
import java.util.List;

public class Rules {

    private List<Rule> rules = new ArrayList<Rule>();
    private String startState;

    public void add(Rule rule) {
        rules.add(rule);
    }

    public Rule startStream(Object state) {
        for (Rule rule : rules) {
            if (rule.getIn() == state && rule.getOn().startStream()) {
                return rule;
            }
        }
        return null;
    }

    public Rule endStream(Object state) {
        for (Rule rule : rules) {
            if (rule.getIn() == state && rule.getOn().endStream()) {
                return rule;
            }
        }
        return null;
    }

    public Rule data(Object state, byte b) {
        for (Rule rule : rules) {
            if (rule.getIn() == state && rule.getOn().data(b)) {
                return rule;
            }
        }
        return null;
    }

    public Rule startRegion(Object state, String name) {
        for (Rule rule : rules) {
            if (rule.getIn() == state && rule.getOn().startRegion(name)) {
                return rule;
            }
        }
        return null;
    }

    public Rule endRegion(Object state, String name) {
        for (Rule rule : rules) {
            if (rule.getIn() == state && rule.getOn().endRegion(name)) {
                return rule;
            }
        }
        return null;
    }

    public void setStartState(String startState) {
        this.startState = startState;
    }

    public String getStartState() {
        return startState;
    }
}
