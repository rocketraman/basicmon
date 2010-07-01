package com.rocketraman.basicmon;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.apache.commons.math.stat.descriptive.rank.Max;
import org.apache.commons.math.stat.descriptive.rank.Min;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A base class for Basic Counters and Timers.
 */
public abstract class BasicMonBase {

    protected String id;
    protected boolean withStats;

    protected AtomicLong counter = new AtomicLong();
    protected Min min = new Min();
    protected Max max = new Max();
    protected Mean mean = new Mean();
    protected Variance variance = new Variance();

    protected BasicMonBase(String id, boolean withStats) {

        this.id = id;
        this.withStats = withStats;

    }

    public void reset() {

        counter.set(0);
        if(! withStats) return;

        synchronized (this) {

            min.clear();
            max.clear();
            mean.clear();
            variance.clear();

        }

    }

    public void increment() {

        long current = counter.incrementAndGet();
        if(! withStats) return;

        updateStats(current);

    }

    public void decrement() {

        long current = counter.decrementAndGet();
        if(! withStats) return;

        updateStats(current);

    }

    public long getCounter() {

        return counter.get();

    }

    public void set(long val) {

        counter.set(val);

        if(! withStats) return;
        updateStats(val);

    }

    public long getMin() {

        synchronized (this) {

            return (long)min.getResult();

        }

    }

    public long getMax() {

        synchronized (this) {

            return (long)max.getResult();

        }

    }

    public double getMean() {

        synchronized (this) {

            return mean.getResult();

        }

    }

    public double getVariance() {

        synchronized (this) {

            return variance.getResult();

        }

    }

    public double getStdDev() {

        double variance;
        long counter = getCounter();

        synchronized (this) {

            variance = getVariance();

        }

        return calcStdDev(variance, counter);

    }

    protected double calcStdDev(double variance, long counter) {

        double stdDev = Double.NaN;
        if (counter > 0) {
            if (counter > 1) {
                stdDev = Math.sqrt(variance);
            } else {
                stdDev = 0.0;
            }
        }
        return stdDev;

    }

    private void updateStats(long val) {

        synchronized (this) {

            min.increment(val);
            max.increment(val);
            mean.increment(val);
            variance.increment(val);

        }

    }

}
