package org.yi.happy.archive.sandbox.interpret;

/**
 * A finite state machine rule.
 */
public class Rule {

    private final OnCondition onCondition;

    private final DoAction doAction;

    private final RuleState goState;

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
    public Rule(OnCondition onCondition, DoAction doAction,
 RuleState goState) {
        this.onCondition = onCondition;
        this.doAction = doAction;
        this.goState = goState;
    }

    public OnCondition getOn() {
        return onCondition;
    }

    public DoAction getAction() {
        return doAction;
    }

    public RuleState getGo() {
        return goState;
    }
}
