package org.yi.happy.archive.sandbox.interpret;

/**
 * An action that can be used in a {@link Rule}.
 */
public interface DoAction {

    /**
     * do the action for a startStream event.
     * 
     * @param callback
     *            the interpreter output.
     */
    void startStream(ActionCallback callback);

    /**
     * do the action for an endStream event.
     * 
     * @param callback
     *            the interpreter output.
     */
    void endStream(ActionCallback callback);

    /**
     * do the action for a data byte.
     * 
     * @param callback
     *            the interpreter output.
     * @param b
     *            the byte.
     */
    void data(ActionCallback callback, byte b);

    /**
     * do the action for a startRegion event.
     * 
     * @param callback
     *            the interpreter output.
     * @param name
     *            the name of the region.
     */
    void startRegion(ActionCallback callback, String name);

    /**
     * do the action for an endRegion event.
     * 
     * @param callback
     *            the interpreter output.
     * @param name
     *            the name of the region.
     */
    void endRegion(ActionCallback callback, String name);

}
