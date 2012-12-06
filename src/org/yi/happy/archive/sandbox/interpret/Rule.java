package org.yi.happy.archive.sandbox.interpret;

/**
 * A finite state machine rule.
 */
public class Rule {

    private final OnCondition onCondition;

    private final DoAction doAction;

    private final State goState;

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
    public Rule(OnCondition onCondition, DoAction doAction, State goState) {
        this.onCondition = onCondition;
        this.doAction = doAction;
        this.goState = goState;
    }

    /**
     * @return the condition for this rule.
     */
    public OnCondition getOn() {
        return onCondition;
    }

    /**
     * @return the action for this rule.
     */
    public DoAction getAction() {
        return doAction;
    }

    /**
     * @return the state to transition to.
     */
    public State getGo() {
        return goState;
    }
}
