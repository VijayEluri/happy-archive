package org.yi.happy.archive.sandbox.interpret;


public class Rule {

    private final Object inState;

    private final OnCondition onCondition;

    private final DoAction doAction;

    private final Object goState;

    public Rule(Object inState, OnCondition onCondition, DoAction doAction,
            Object goState) {
        this.inState = inState;
        this.onCondition = onCondition;
        this.doAction = doAction;
        this.goState = goState;
    }

    public Object getIn() {
        return inState;
    }

    public OnCondition getOn() {
        return onCondition;
    }

    public DoAction getAction() {
        return doAction;
    }

    public Object getGo() {
        return goState;
    }
}
