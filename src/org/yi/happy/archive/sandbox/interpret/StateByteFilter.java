package org.yi.happy.archive.sandbox.interpret;

import org.yi.happy.archive.tag.BinaryHandler;

/**
 * A state machine, using the state pattern, based filter for a binary event
 * stream. Every data byte that comes in will be passed on, the markings are
 * manipulated. Very good for marking up a binary stream for further processing.
 */
public class StateByteFilter implements BinaryHandler {
    private OutputHandler callback;

    private State state;

    /**
     * set up a rule based interpreter for doing simple transformations to a
     * labeled binary stream using a finite state machine.
     * 
     * @param state
     *            the starting state.
     * @param handler
     *            the output handler.
     */
    public StateByteFilter(State state, BinaryHandler handler) {
        this.callback = new OutputHandler(handler);
        this.state = state;
    }

    @Override
    public void startStream() {
        state = state.startStream(callback);
    }

    @Override
    public void startRegion(String name) {
        state = state.startRegion(name, callback);
    }

    @Override
    public void bytes(byte[] buff, int offset, int length) {
        callback.startBytes(buff, offset, length);
        try {
            while (callback.hasCurrentByte()) {
                state = state.data(callback.getCurrentByte(), callback);
            }
        } finally {
            callback.endBytes();
        }
    }

    @Override
    public void endRegion(String name) {
        state = state.endRegion(name, callback);
    }

    @Override
    public void endStream() {
        state = state.endStream(callback);
    }

}
