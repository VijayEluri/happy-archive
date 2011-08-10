package org.yi.happy.archive;

import java.io.PrintStream;

public class NullPrintStream extends PrintStream {
    public NullPrintStream() {
        super(new NullOutputStream());
    }
}
