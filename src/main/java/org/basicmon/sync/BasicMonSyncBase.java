package org.basicmon.sync;

import org.basicmon.BasicMonBase;

/**
 * A base class for counters and timers using primitive counters and synchronization. It stores an arbitrary
 * value along with its count and total used for calculating its average. If @{link #withStats} is true, then it also
 * stores the min, max, and variance of the value.
 */
public abstract class BasicMonSyncBase extends BasicMonBase {

    private long val;
    private long total;
    private long updateCount;

    public BasicMonSyncBase(String id, boolean withStats) {

        super(id, withStats);

    }

    @Override
    public synchronized void reset() {

        val = 0;
        total = 0;
        updateCount = 0;

        super.reset();

    }

    @Override
    public synchronized void increment() {

        long current = ++val;
        total += current;
        updateCount++;

        if(! withStats) return;
        updateStats(current);

    }

    @Override
    public synchronized void decrement() {

        long current = --val;
        total += current;
        updateCount++;

        if(! withStats) return;
        updateStats(current);

    }

    @Override
    public synchronized void setVal(long val) {

        this.val = val;
        total += val;
        updateCount++;

        if(! withStats) return;
        updateStats(val);

    }

    @Override
    public synchronized long getVal() {

        return val;

    }

    @Override
    public synchronized long getTotal() {

        return total;

    }

    @Override
    public synchronized long getUpdateCount() {

        return updateCount;

    }

    @Override
    public synchronized double getMean() {

        return (double)total / (double) updateCount;

    }

}
