package org.yi.happy.archive.sandbox.interpret;

/**
 * A finite state machine rule.
 */
public class Rule<Type> {

    private final Type inState;

    private final OnCondition onCondition;

    private final DoAction doAction;

    private final Type goState;

    /**
     * create a finite state machine rule.
     * 
     * @param inState
     *            the state this rule applies.
     * @param onCondition
     *            the condition for this rule.
     * @param doAction
     *            the action to be done.
     * @param goState
     *            the state to transition to.
     */
    public Rule(Type inState, OnCondition onCondition, DoAction doAction,
            Type goState) {
        this.inState = inState;
        this.onCondition = onCondition;
        this.doAction = doAction;
        this.goState = goState;
    }

    public Type getIn() {
        return inState;
    }

    public OnCondition getOn() {
        return onCondition;
    }

    public DoAction getAction() {
        return doAction;
    }

    public Type getGo() {
        return goState;
    }
}
