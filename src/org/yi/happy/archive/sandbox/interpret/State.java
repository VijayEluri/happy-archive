package org.yi.happy.archive.sandbox.interpret;

/**
 * The base of the state pattern implementation for {@link InterpretFilter}.
 */
public interface State {
    /**
     * handle a start of region event.
     * 
     * @param name
     *            the name of the region.
     * @param callback
     *            where to send results.
     * @return the resulting state.
     */
    State startRegion(String name, ActionCallback callback);

    /**
     * handle an end of region event.
     * 
     * @param name
     *            the name of the region.
     * @param callback
     *            where to send results.
     * @return the resulting state.
     */
    State endRegion(String name, ActionCallback callback);

    /**
     * handle a data byte event.
     * 
     * @param b
     *            the data byte.
     * @param callback
     *            where to send results.
     * @return the resulting state.
     */
    State data(byte b, ActionCallback callback);

    /**
     * handle a start of stream event.
     * 
     * @param callback
     *            where to send results.
     * @return the resulting state.
     */
    State startStream(ActionCallback callback);

    /**
     * handle an end of stream event.
     * 
     * @param callback
     *            where to send results.
     * @return the resulting state.
     */
    State endStream(ActionCallback callback);
}
