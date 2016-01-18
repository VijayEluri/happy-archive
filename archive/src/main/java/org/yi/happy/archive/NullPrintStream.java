package org.yi.happy.archive;

import java.io.PrintStream;

/**
 * A PrintStream that does nothing. Useful for testing where output is ignored.
 */
public class NullPrintStream extends PrintStream {
    /**
     * Set up a PrintStream that does nothing.
     */
    public NullPrintStream() {
        super(new NullOutputStream());
    }
}
