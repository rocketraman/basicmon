package org.basicmon.sync;

import org.basicmon.BasicTimer;
import org.basicmon.BasicTimerSplit;
import org.basicmon.BasicTimerStats;
import org.basicmon.util.BasicMonUtil;
import org.basicmon.util.BasicTimerUtil;

import java.util.concurrent.Callable;

/**
 * A basic timer keeping a count and cumulative time. The val in {@link org.basicmon.atomic.BasicMonAtomicBase} will represent the
 * timer split times.
 * <p>
 * This implementation uses thread synchronization to keep its internal counts. This is a good choice for a timer
 * implementation when the code being timed is expected to be accessed by many threads concurrently. In this case the
 * pessimistic synchronization performed by the synchronized keyword is preferred to the optimistic synchronization
 * performed by the Atomic classes. This implementation also seems to have very good performance when only one thread
 * is accessing the timed code -- it seems likely that java uses optimizations to eliminate unnecessary synchronization
 * in these cases.
 */
public final class BasicTimerSyncImpl<V> extends BasicMonSyncBase implements BasicTimer<V> {

    private int active;
    private long activeTotal;
    private long activeCounter;

    // advanced stats (slower)
    private BasicTimerStats basicTimerStats;

    public BasicTimerSyncImpl(String id) {

        super(id, false);

    }

    public BasicTimerSyncImpl(String id, boolean withStats) {

        super(id, withStats);

        if(withStats) {

            try {

                basicTimerStats = new BasicTimerStats();

            } catch (NoClassDefFoundError e) {

                /* no commons-math */

            }

        }

    }

    @Override
    public synchronized void reset() {

        active = 0;
        activeTotal = 0;
        activeCounter = 0;

        super.reset();

    }

    public BasicTimerSplit start() {

        updateActive();

        // start timing as late as possible, get expensive nanoTime call out of synchronized block
        return new BasicTimerSplit(this, System.nanoTime());

    }

    public long stop(BasicTimerSplit split) {

        // stop timing as soon as possible
        final long splitTime = System.nanoTime() - split.getStartTime();
        updateSplit(splitTime);

        return splitTime;

    }

    public V doInTimer(Callable<V> function) throws Exception {

        BasicTimerSplit split = start();

        try {

            return function.call();

        } finally {

            split.stop();

        }

    }

    public void addTime(long time) {

        updateSplit(time);

    }

    public synchronized int getActive() {

        return active;

    }

    public synchronized double getActiveMean() {

        return (double) activeTotal / (double)activeCounter;

    }

    @Override
    public long getActiveMax() {

        return withStats ? basicTimerStats.getActiveMax() : -1;

    }

    @Override
    public double getActiveVariance() {

        return withStats ? basicTimerStats.getActiveVariance() : -1;

    }

    public synchronized double getActiveStdDev() {

        if(! withStats) return -1;

        double variance = getActiveVariance();
        long counter = getVal();

        return BasicMonUtil.calcStdDev(variance, counter);

    }

    @Override
    public String toString() {

        return BasicTimerUtil.toString(this);

    }

    private synchronized void updateActive() {

        final int currentActive = ++active;
        if(withStats) basicTimerStats.updateActiveStats(currentActive);
        activeTotal += currentActive;
        activeCounter++;

    }

    private synchronized void updateSplit(long splitTime) {

        active--;

        super.setVal(splitTime);

    }

}
