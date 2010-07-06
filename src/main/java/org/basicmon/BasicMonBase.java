package org.basicmon;

import org.basicmon.util.BasicMonUtil;

/**
 * The BasicMonBase class.
 */
public abstract class BasicMonBase {

    protected final String id;
    protected final boolean withStats;

    // advanced stats (slower)
    private BasicMonStats basicMonStats;

    public BasicMonBase(String id, boolean withStats) {

        this.id = id;

        if(withStats) {

            try {

                basicMonStats = new BasicMonStats();

            } catch (NoClassDefFoundError e) {

                /* no commons-math */
                this.withStats = false;
                return;

            }

        }

        this.withStats = withStats;

    }

    public final String getId() {

        return id;

    }

    public final boolean isWithStats() {

        return withStats;

    }

    public void reset() {

        if(withStats) basicMonStats.reset();

    }

    public abstract void increment();

    public abstract void decrement();

    public abstract void setVal(long val);

    public abstract long getTotal();

    public abstract long getUpdateCount();

    public abstract double getMean();

    public final long getMin() {

        return withStats ? basicMonStats.getMin() : -1;

    }

    public final long getMax() {

        return withStats ? basicMonStats.getMax() : -1;

    }

    public final double getVariance() {

        return withStats ? basicMonStats.getVariance() : -1;

    }

    public abstract long getVal();

    public final double getStdDev() {

        if(! withStats) return -1;

        long counter = getVal();
        double variance = getVariance();

        return BasicMonUtil.calcStdDev(variance, counter);

    }

    /**
     * Updates the advanced stats. To prevent an unnecessary method call, ensure withStats is true before calling this
     * method.
     * @param val The value to update stats with.
     */
    protected void updateStats(long val) {

        if(! withStats) return;

        basicMonStats.updateStats(val);

    }

}
