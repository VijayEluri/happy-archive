package org.yi.happy.archive;

import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * A waiting strategy that uses a progressive delay to sixty seconds.
 */
public class WaitHandlerProgressiveDelay implements WaitHandler {
    private int delay = 1;
    private int lastDelay = 0;

    @Override
    public void doWait(boolean progress) throws IOException {
	try {
	    if (progress) {
		delay = 1;
		lastDelay = 0;

		Thread.sleep(delay * 1000);
		return;
	    }

	    Thread.sleep(delay * 1000);

	    int nextDelay = delay + lastDelay;
	    lastDelay = delay;
	    delay = nextDelay;

	    if (delay > 60) {
		delay = 60;
	    }
	} catch (InterruptedException e) {
	    throw new InterruptedIOException();
	}
    }
}
