package org.yi.happy.archive.binary_stream;

/**
 * An action that sends a startRegion event.
 */
public class DoStartRegion implements DoAction {
    private final String name;

    /**
     * create an action that sends a startRegion event.
     * 
     * @param name
     *            the name of the region.
     */
    public DoStartRegion(String name) {
        this.name = name;
    }
    private void action(ActionCallback callback) {
        callback.startRegion(name);
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
