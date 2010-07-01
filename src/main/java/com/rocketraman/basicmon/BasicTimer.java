package com.rocketraman.basicmon;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.apache.commons.math.stat.descriptive.rank.Max;
import org.javasimon.utils.SimonUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A basic timer keeping a count and cumulative time.
 */
public class BasicTimer extends BasicMonBase {

    private AtomicLong total = new AtomicLong(0);
    private AtomicInteger active = new AtomicInteger(0);
    protected Max activeMax = new Max();
    protected Mean activeMean = new Mean();
    protected Variance activeVariance = new Variance();

    public BasicTimer(String id) {

        super(id, false);

    }

    public BasicTimer(String id, boolean withStats) {

        super(id, withStats);

    }

    public void reset() {

        total.set(0);
        super.reset();

    }

    public BasicTimerSplit start() {

        int currentActive = active.incrementAndGet();

        if(withStats) updateActiveStats(currentActive);

        // start timing as late as possible
        return new BasicTimerSplit(this, System.nanoTime());

    }

    public void stop(BasicTimerSplit split) {

        // stop timing as soon as possible
        final long splitTime = System.nanoTime() - split.getStartTime();
        active.decrementAndGet();
        total.addAndGet(splitTime);
        increment();

    }

    public long getTotal() {

        return total.get();

    }

    public int getActive() {

        return active.get();

    }

    public long getActiveMax() {

        synchronized (this) {

            return (long)activeMax.getResult();

        }

    }

    public double getActiveMean() {

        synchronized (this) {

            return activeMean.getResult();

        }

    }

    public double getActiveVariance() {

        synchronized (this) {

            return activeVariance.getResult();

        }

    }

    public double getActiveStdDev() {

        double variance = getActiveVariance();
        long counter = getCounter();

        return calcStdDev(variance, counter);

    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder(256)
            .append("BasicTimer: [").append(id).append("]")
            .append(" total ").append(SimonUtils.presentNanoTime(getTotal()))
            .append(", counter ").append(getCounter());

        if(withStats) {

            s.append(", mean ").append(SimonUtils.presentNanoTime((long)getMean()))
                .append(", min ").append(SimonUtils.presentNanoTime(getMin()))
                .append(", max ").append(SimonUtils.presentNanoTime(getMax()))
                .append(", stddev ").append(SimonUtils.presentNanoTime((long)getStdDev()));

        }

        s.append(", active ").append(getActive());

        if(withStats) {

            s.append(", active mean ").append(getActiveMean())
                .append(", active max ").append(getActiveMax())
                .append(", active stddev ").append(getActiveStdDev());

        }

        return s.toString();

    }

    private void updateActiveStats(long val) {

        synchronized (this) {

            activeMax.increment(val);
            activeMean.increment(val);
            activeVariance.increment(val);

		}

    }

}
