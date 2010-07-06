package org.basicmon;

/**
 * An interface for a BasicTimer.
 */
public interface BasicTimer extends BasicMon {

    public BasicTimerSplit start();

    public long stop(BasicTimerSplit split);

    public void addTime(long time);

    public int getActive();

    public double getActiveMean();

    public long getActiveMax();

    public double getActiveVariance();

    public double getActiveStdDev();

}
