package org.yi.happy.archive.sandbox.interpret;

public class DoCopy implements DoAction {
    @Override
    public void startStream(ActionCallback callback) {
        callback.startStream();
    }

    @Override
    public void endStream(ActionCallback callback) {
        callback.endStream();
    }

    @Override
    public void data(ActionCallback callback, byte b) {
        callback.consumeAndSend();
    }

    @Override
    public void startRegion(ActionCallback callback, String name) {
        callback.startRegion(name);
    }

    @Override
    public void endRegion(ActionCallback callback, String name) {
        callback.endRegion(name);
    }
}
