package org.yi.happy.archive.sandbox.interpret;

/**
 * An action that sends an endRegion event.
 */
public class DoEndRegion implements DoAction {
    private final String name;

    /**
     * create an action that sends an endRegion event.
     * 
     * @param name
     *            the name of the region.
     */
    public DoEndRegion(String name) {
        this.name = name;
    }

    private void action(ActionCallback callback) {
        callback.endRegion(name);
    }

    @Override
    public void startStream(ActionCallback callback) {
        action(callback);
    }

    @Override
    public void endStream(ActionCallback callback) {
        action(callback);
    }

    @Override
    public void data(ActionCallback callback, byte b) {
        action(callback);
    }

    @Override
    public void startRegion(ActionCallback callback, String name) {
        action(callback);
    }

    @Override
    public void endRegion(ActionCallback callback, String name) {
        action(callback);
    }

}
