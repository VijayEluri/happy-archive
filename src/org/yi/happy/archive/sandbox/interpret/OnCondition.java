package org.yi.happy.archive.sandbox.interpret;

public interface OnCondition {

    boolean startStream();

    boolean endStream();

    boolean data(byte b);

    boolean startRegion(String name);

    boolean endRegion(String name);
}
