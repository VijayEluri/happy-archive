package org.yi.happy.archive.sandbox.interpret;

public class DoSend implements DoAction {

    @Override
    public void startStream(ActionCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void endStream(ActionCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void data(ActionCallback callback, byte b) {
        callback.send();
    }

    @Override
    public void startRegion(ActionCallback callback, String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void endRegion(ActionCallback callback, String name) {
        throw new UnsupportedOperationException();
    }

}
