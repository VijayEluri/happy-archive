package org.yi.happy.archive.join;

import org.yi.happy.archive.Bytes;

public interface FragmentHandler {

    void data(long offset, Bytes data);

    void end();
}
