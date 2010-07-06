package org.basicmon;

/**
 * A basic timer keeping a count and cumulative time.
 */
public final class BasicTimerSplit {

    private final BasicTimer timer;
    private final long startTime;

    public BasicTimerSplit(final BasicTimer timer, final long startTime) {

        this.timer = timer;
        this.startTime = startTime;

    }

    public long stop() {

        return timer.stop(this);

    }

    public long getStartTime() {

        return startTime;

    }

}
