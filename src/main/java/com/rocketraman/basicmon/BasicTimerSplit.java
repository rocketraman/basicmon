package com.rocketraman.basicmon;

/**
 * A basic timer keeping a count and cumulative time.
 */
public class BasicTimerSplit {

    private BasicTimer timer;
    private long startTime;

    public BasicTimerSplit(BasicTimer timer, long startTime) {

        this.timer = timer;
        this.startTime = startTime;

    }

    public void stop() {

        timer.stop(this);

    }

    public long getStartTime() {

        return startTime;

    }

}
