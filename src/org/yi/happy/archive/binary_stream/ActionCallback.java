package org.yi.happy.archive.binary_stream;

/**
 * The sending side of the state machine.
 */
public interface ActionCallback {

    /**
     * send on a startStream event.
     */
    void startStream();

    /**
     * send on an endStream event.
     */
    void endStream();

    /**
     * send on the byte.
     */
    void consumeAndSend();

    /**
     * send on a startRegion event.
     * 
     * @param name
     *            the name of the region.
     */
    void startRegion(String name);

    /**
     * send on a endRegion event.
     * 
     * @param name
     *            the name of the region.
     */
    void endRegion(String name);
}
