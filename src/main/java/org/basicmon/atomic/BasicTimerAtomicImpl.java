package org.basicmon.atomic;

import org.basicmon.BasicTimer;
import org.basicmon.BasicTimerSplit;
import org.basicmon.BasicTimerStats;
import org.basicmon.util.BasicMonUtil;
import org.basicmon.util.BasicTimerUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A basic timer keeping a count and cumulative time. The val in {@link BasicMonAtomicBase} will represent the
 * timer split times.
 * <p>
 * This implementation uses the java.util.concurrent.atomic.* classes to keep its internal counts, and only uses
 * synchronization for additional statistics. This is a good default choice for a timer implementation because it
 * performs very well on most platforms in low thread contention scenarios. For high thread contention scenarios,
 * use the synchronized implementation {@link org.basicmon.sync.BasicTimerSyncImpl}.
 */
public final class BasicTimerAtomicImpl<V> extends BasicMonAtomicBase implements BasicTimer<V> {

    private final AtomicInteger active = new AtomicInteger(0);
    private final AtomicLong activeTotal = new AtomicLong(0);
    private final AtomicLong activeCounter = new AtomicLong(0);

    // advanced stats (slower)
    private BasicTimerStats basicTimerStats;

    public BasicTimerAtomicImpl(String id) {

        super(id, false);

    }

    public BasicTimerAtomicImpl(String id, boolean withStats) {

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

        active.set(0);
        activeTotal.set(0);
        activeCounter.set(0);

        super.reset();

    }

    public BasicTimerSplit start() {

        final int currentActive = active.incrementAndGet();
        activeTotal.addAndGet(currentActive);
        activeCounter.incrementAndGet();

        if(withStats) basicTimerStats.updateActiveStats(currentActive);

        // start timing as late as possible
        return new BasicTimerSplit(this, System.nanoTime());

    }

    public long stop(BasicTimerSplit split) {

        // stop timing as soon as possible
        final long splitTime = System.nanoTime() - split.getStartTime();
        active.decrementAndGet();

        super.setVal(splitTime);

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

        super.setVal(time);

    }

    public int getActive() {

        return active.get();

    }

    public double getActiveMean() {

        return (double) activeTotal.get() / (double)activeCounter.get();

    }

    @Override
    public long getActiveMax() {

        return withStats ? basicTimerStats.getActiveMax() : -1;

    }

    @Override
    public double getActiveVariance() {

        return withStats ? basicTimerStats.getActiveVariance() : -1;

    }

    public double getActiveStdDev() {

        if(! withStats) return -1;

        double variance = getActiveVariance();
        long counter = getVal();

        return BasicMonUtil.calcStdDev(variance, counter);

    }

    @Override
    public String toString() {

        return BasicTimerUtil.toString(this);

    }

}
