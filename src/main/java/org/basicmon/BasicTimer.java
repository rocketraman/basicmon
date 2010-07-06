package org.basicmon;

/**
 * An interface for a BasicTimer.
 */
public interface BasicTimer extends BasicMon {

    public BasicTimerSplit start();

    public void stop(BasicTimerSplit split);

    public int getActive();

    public double getActiveMean();

    public long getActiveMax();

    public double getActiveVariance();

    public double getActiveStdDev();

}
