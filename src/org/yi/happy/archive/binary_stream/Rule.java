package org.yi.happy.archive.binary_stream;

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
     * create a finite state machine rule.
     * 
     * @param onCondition
     *            the condition for this rule.
     * @param goState
     *            the state to transition to.
     * @param doAction
     *            the action to be done.
     */
    public Rule(OnCondition onCondition, State goState, DoAction... doAction) {
        this.onCondition = onCondition;
        this.goState = goState;

        if (doAction.length == 1) {
            this.doAction = doAction[0];
        } else {
            this.doAction = new DoAll(doAction);
        }
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
