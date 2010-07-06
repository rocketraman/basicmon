package org.basicmon;

/**
 * An interface for all Basic Monitors. One of the subclass interfaces {@link BasicTimer} or {@link BasicCounter}
 * would generally be used.
 */
public interface BasicMon {

    public String getId();

    public boolean isWithStats();

    public void reset();

    public long getTotal();

    public long getUpdateCount();

    public double getMean();

    public long getMin();

    public long getMax();

    public double getStdDev();

}
