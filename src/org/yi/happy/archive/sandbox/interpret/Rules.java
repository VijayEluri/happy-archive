package org.yi.happy.archive.sandbox.interpret;

import java.util.IdentityHashMap;

/**
 * Finite state machine rules for {@link InterpretFilter}.
 * 
 * @param <Type>
 *            the type of the state points in the rule.
 */
public class Rules<Type> {

    private IdentityHashMap<Type, RuleState> states = new IdentityHashMap<Type, RuleState>();
    private Type startState;

    /**
     * Add a rule to the set of rules.
     * 
     * @param rule
     *            the rule to add.
     */
    public void add(Rule<Type> rule) {
        RuleState in = states.get(rule.getIn());
        if (in == null) {
            in = new RuleState();
            states.put(rule.getIn(), in);
        }
        RuleState go = states.get(rule.getGo());
        if (go == null) {
            go = new RuleState();
            states.put(rule.getGo(), go);
        }

        in.add(new Rule<RuleState>(in, rule.getOn(), rule.getAction(), go));
    }

    public RuleState getState(Type state) {
        return states.get(state);
    }
}
