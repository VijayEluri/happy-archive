package org.yi.happy.archive.sandbox.interpret;

public interface DoAction {

    void startStream(ActionCallback callback);

    void endStream(ActionCallback callback);

    void data(ActionCallback callback, byte b);

    void startRegion(ActionCallback callback, String name);

    void endRegion(ActionCallback callback, String name);

}
