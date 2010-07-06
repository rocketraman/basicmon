package org.basicmon;

import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.apache.commons.math.stat.descriptive.rank.Max;
import org.apache.commons.math.stat.descriptive.rank.Min;

/**
 * Keeps advanced stats, if enabled, for any BasicMon.
 */
public final class BasicMonStats {

    private Min min;
    private Max max;
    private Variance variance;

    public BasicMonStats() {

        this.min = new Min();
        this.max = new Max();
        this.variance = new Variance();

    }

    public synchronized void reset() {

        min.clear();
        max.clear();
        variance.clear();

    }

    public final synchronized long getMin() {

        return (long)min.getResult();

    }

    public final synchronized long getMax() {

        return (long)max.getResult();

    }

    public final synchronized double getVariance() {

        return variance.getResult();

    }

    /**
     * Updates the advanced stats. To prevent an unnecessary method call, ensure withStats is true before calling this
     * method.
     * @param val The value to update stats with.
     */
    protected synchronized void updateStats(long val) {

        min.increment(val);
        max.increment(val);
        variance.increment(val);

    }

}
