package org.yi.happy.metric;

/**
 * I am a summary of many timers. I am useful for finding out how long a process
 * takes by adding very little to the process.
 * 
 * <pre>
 * {@link SimpleTimer} t = times.getTimer();
 * try {
 *     // do work
 * } finally {
 *     t.stop();
 * }
 * </pre>
 */
public class SimpleTimerSummary {

    /**
     * create with no stats.
     */
    public SimpleTimerSummary() {
	started = 0;
	count = 0;
	totalTime = 0;
	highTime = 0;
	secondHighTime = 0;
	lowTime = 0;
	lastTime = 0;
    }

    /**
     * how I collect the stats.
     * 
     * @author sarah dot a dot happy at gmail dot com
     * 
     */
    private class MyTimer extends SimpleTimer {
	/**
	 * in addition to stopping the clock record the time.
	 */
	@Override
	public void stop() {
	    super.stop();
	    registerTime(getTime());
	}
    }

    /**
     * record a time
     * 
     * @param time
     *            the time to record in ms
     */
    private synchronized void registerTime(long time) {
	lastTime = time;

	if (count == 0) {
	    lowTime = time;
	} else if (time < lowTime) {
	    lowTime = time;
	}

	if (time > highTime) {
	    secondHighTime = highTime;
	    highTime = time;
	} else if (time > secondHighTime) {
	    secondHighTime = time;
	}

	totalTime += time;
	count += 1;
    }

    /**
     * how many timer have been stopped
     */
    private int count;

    /**
     * the total of all the times in ms
     */
    private long totalTime;

    /**
     * the highest time in ms
     */
    private long highTime;

    /**
     * the lowest time in ms
     */
    private long lowTime;

    /**
     * the second highest time in ms
     */
    private long secondHighTime;

    /**
     * the last recorded time
     */
    private long lastTime;

    /**
     * how many timers have started
     */
    private int started;

    /**
     * get a started timer. It is expected that stop will be called on the
     * returned timer.
     * 
     * There is no reference kept to the timer so not stopping them does not
     * cause a memory leak.
     * 
     * @return a timer
     */
    public SimpleTimer getTimer() {
	synchronized (this) {
	    started += 1;
	}
	return new MyTimer();
    }

    /**
     * get the number of timers that have been started but not stopped.
     * 
     * @return the number of active timers
     */
    public synchronized int getActiveCount() {
	return started - count;
    }

    /**
     * get the number of stopped timers
     * 
     * @return the count
     */
    public synchronized int getCount() {
	return count;
    }

    /**
     * get the total of all stopped timers.
     * 
     * @return the total time in ms, zero if no timers have stopped
     */
    public synchronized long getTotalTime() {
	return totalTime;
    }

    /**
     * get the highest time of all stopped timers
     * 
     * @return the highest time in ms, zero if no timers have stopped
     */
    public synchronized long getHighTime() {
	return highTime;
    }

    /**
     * get the second highest time. I record this since the highest time may
     * include class loading time.
     * 
     * @return the second highest time in ms, zero if no timers have stopped
     */
    public synchronized long getSecondHighTime() {
	return secondHighTime;
    }

    /**
     * get the average time of all stopped timers
     * 
     * @return the average time in ms, zero if no timers have stopped
     */
    public synchronized long getAverageTime() {
	if (count == 0) {
	    return 0;
	}
	return totalTime / count;
    }

    /**
     * get the lowest time of all stopped timers.
     * 
     * @return the lowest time in ms, zero if no timers have stopped
     */
    public synchronized long getLowTime() {
	return lowTime;
    }

    /**
     * get the time of the last stopped timer.
     * 
     * @return the last time.
     */
    public synchronized long getLastTime() {
	return lastTime;
    }

    /**
     * get a summary string.
     * 
     * @return a string representation of the summary data.
     */
    @Override
    public synchronized String toString() {
	return "[active = " + getActiveCount() + ", count = " + getCount()
		+ ", high = " + getHighTime() + ", secondHigh = "
		+ getSecondHighTime() + ", average = " + getAverageTime()
		+ ", low = " + getLowTime() + ", last = " + getLastTime() + "]";
    }
}
