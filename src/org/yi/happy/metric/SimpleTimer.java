package org.yi.happy.metric;

/**
 * I am a simple timer.
 */
public class SimpleTimer {

    /**
     * create started.
     */
    public SimpleTimer() {
        startTime = System.currentTimeMillis();
        stopTime = startTime - 1;
    }

    /**
     * when the timer started.
     */
    private long startTime;

    /**
     * when the timer was stopped. (initially: startTime - 1)
     */
    private long stopTime;

    /**
     * stop the timer. A timer may only be stopped once.
     */
    public void stop() {
        if (stopTime >= startTime) {
            throw new IllegalStateException("stop may only be called once");
        }
        stopTime = System.currentTimeMillis();
    }

    /**
     * get the time on the timer. if the timer has not been stopped get the time
     * from when it was started to now. if the timer has been stopped get the
     * time between when it was started and when it was stopped.
     * 
     * @return the elapsed time in ms
     */
    public long getTime() {
        if (stopTime < startTime) {
            return System.currentTimeMillis() - startTime;
        }
        return stopTime - startTime;
    }
}
