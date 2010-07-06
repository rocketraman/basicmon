package org.basicmon;

import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.apache.commons.math.stat.descriptive.rank.Max;

/**
 * Keeps advanced stats, if enabled, for any BasicTimer.
 */
public final class BasicTimerStats {

    private Max activeMax;
    private Variance activeVariance;

    public BasicTimerStats() {

        this.activeMax = new Max();
        this.activeVariance = new Variance();

    }

    public synchronized void reset() {

        activeMax.clear();
        activeVariance.clear();

    }

    public synchronized long getActiveMax() {

        return (long)activeMax.getResult();

    }

    public synchronized double getActiveVariance() {

        return activeVariance.getResult();

    }

    public synchronized void updateActiveStats(long val) {

        activeMax.increment(val);
        activeVariance.increment(val);

    }

}
