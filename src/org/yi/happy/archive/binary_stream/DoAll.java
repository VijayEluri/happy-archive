package org.yi.happy.archive.binary_stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An action that does a list of actions.
 */
public class DoAll implements DoAction {
    private List<DoAction> actions;

    /**
     * create an action that does a list of actions.
     * 
     * @param actions
     *            the list of actions.
     */
    public DoAll(DoAction... actions) {
        this.actions = new ArrayList<DoAction>(Arrays.asList(actions));
    }

    @Override
    public void startStream(ActionCallback callback) {
        for (DoAction action : actions) {
            action.startStream(callback);
        }
    }

    @Override
    public void endStream(ActionCallback callback) {
        for (DoAction action : actions) {
            action.endStream(callback);
        }
    }

    @Override
    public void data(ActionCallback callback, byte b) {
        for (DoAction action : actions) {
            action.data(callback, b);
        }
    }

    @Override
    public void startRegion(ActionCallback callback, String name) {
        for (DoAction action : actions) {
            action.startRegion(callback, name);
        }
    }

    @Override
    public void endRegion(ActionCallback callback, String name) {
        for (DoAction action : actions) {
            action.endRegion(callback, name);
        }
    }

}
