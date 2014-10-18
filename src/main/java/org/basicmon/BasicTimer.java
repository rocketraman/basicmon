package org.basicmon;

import java.util.concurrent.Callable;

/**
 * An interface for a BasicTimer.
 */
public interface BasicTimer<V> extends BasicMon {

    public BasicTimerSplit start();

    public long stop(BasicTimerSplit split);

    public V doInTimer(Callable<V> function) throws Exception;

    public void addTime(long time);

    public int getActive();

    public double getActiveMean();

    public long getActiveMax();

    public double getActiveVariance();

    public double getActiveStdDev();

}
