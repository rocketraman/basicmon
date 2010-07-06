package org.basicmon.atomic;

import org.basicmon.BasicMonBase;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A base class for counters and timers using the {@link java.util.concurrent.atomic} classes. It stores an arbitrary
 * value along with its update count and total used for calculating its average. If @{link #withStats} is true, then
 * it also stores the min, max, and variance of the value.
 */
public abstract class BasicMonAtomicBase extends BasicMonBase {

    private final AtomicLong val = new AtomicLong(0);
    private final AtomicLong total = new AtomicLong(0);
    private final AtomicLong updateCount = new AtomicLong(0);

    public BasicMonAtomicBase(String id, boolean withStats) {

        super(id, withStats);

    }

    @Override
    public synchronized void reset() {

        val.set(0);
        total.set(0);
        updateCount.set(0);

        super.reset();

    }

    @Override
    public void increment() {

        long current = val.incrementAndGet();
        total.addAndGet(current);
        updateCount.incrementAndGet();

        if(! withStats) return;
        updateStats(current);

    }

    @Override
    public void decrement() {

        long current = val.decrementAndGet();
        total.addAndGet(current);
        updateCount.incrementAndGet();

        if(! withStats) return;
        updateStats(current);

    }

    @Override
    public void setVal(long val) {

        this.val.set(val);
        total.addAndGet(val);
        updateCount.incrementAndGet();

        if(! withStats) return;
        updateStats(val);

    }

    @Override
    public long getVal() {

        return val.get();

    }

    @Override
    public long getTotal() {

        return total.get();

    }

    @Override
    public long getUpdateCount() {

        return updateCount.get();

    }

    @Override
    public double getMean() {

        return (double)total.get() / (double) updateCount.get();

    }

}
