package org.yi.happy.archive.sandbox.interpret;

public interface ActionCallback {

    void startStream();

    void endStream();

    void send();

    void startRegion(String name);

    void endRegion(String name);

}
