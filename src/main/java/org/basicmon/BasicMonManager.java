package org.basicmon;

import org.basicmon.atomic.BasicCounterAtomicImpl;
import org.basicmon.atomic.BasicTimerAtomicImpl;
import org.basicmon.sync.BasicCounterSyncImpl;
import org.basicmon.sync.BasicTimerSyncImpl;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A manager for BasicTimer and BasicCounterAtomicImpl instances. BasicTimer is used to provide some (basic) timing information during
 * testing and debugging. BasicCounterAtomicImpl provides count information, with associated stats.
 */
public final class BasicMonManager {

    private static final BasicMonManager manager = new BasicMonManager();

    private final SortedMap<String,BasicTimer> timers = new TreeMap<String, BasicTimer>();
    private final SortedMap<String,BasicCounter> counters = new TreeMap<String, BasicCounter>();

    public static BasicTimer getTimer(String id) {

        return manager.doGetAtomicTimer(id, false);

    }

    public static BasicTimer getTimerWithStats(String id) {

        return manager.doGetAtomicTimer(id, true);

    }

    public static BasicTimer getTimer(String id, boolean withStats) {

        return manager.doGetAtomicTimer(id, withStats);

    }

    public static BasicTimer getSyncTimer(String id) {

        return manager.doGetSyncTimer(id, false);

    }

    public static BasicTimer getSyncTimerWithStats(String id) {

        return manager.doGetSyncTimer(id, true);

    }

    public static BasicTimer getSyncTimer(String id, boolean withStats) {

        return manager.doGetSyncTimer(id, withStats);

    }

    public static BasicTimer getAtomicTimer(String id) {

        return manager.doGetAtomicTimer(id, false);

    }

    public static BasicTimer getAtomicTimerWithStats(String id) {

        return manager.doGetAtomicTimer(id, true);

    }

    public static BasicTimer getAtomicTimer(String id, boolean withStats) {

        return manager.doGetAtomicTimer(id, withStats);

    }

    public static BasicCounter getCounter(String id) {

        return manager.doGetSyncCounter(id, false);

    }

    public static BasicCounter getCounterWithStats(String id) {

        return manager.doGetSyncCounter(id, true);

    }

    public static BasicCounter getCounter(String id, boolean withStats) {

        return manager.doGetSyncCounter(id, withStats);

    }

    public static BasicCounter getAtomicCounter(String id) {

        return manager.doGetAtomicCounter(id, false);

    }

    public static BasicCounter getAtomicCounterWithStats(String id) {

        return manager.doGetAtomicCounter(id, true);

    }

    public static BasicCounter getAtomicCounter(String id, boolean withStats) {

        return manager.doGetAtomicCounter(id, withStats);

    }

    public static String prettyPrint() {

        return manager.doPrettyPrint();

    }

    public static void reset() {

        manager.doReset();

    }

    /**
     * Get a BasicTimer implemented using atomic classes. Creates it if it does not exist. Note that if the
     * same ID already exists with a different implementation or with different stats setting, the earlier timer
     * will be returned.
     * @param id The id of the timer to assign or get.
     * @param withStats Whether to record stats or not. If true, the timer will be much slower.
     * @return The BasicTimer.
     */
    private BasicTimer doGetAtomicTimer(String id, boolean withStats) {

        synchronized (timers) {

            BasicTimer timer = timers.get(id);

            if(timer == null) {

                timer = new BasicTimerAtomicImpl(id, withStats);
                timers.put(id, timer);

            }

            return timer;

        }

    }

    /**
     * Get a BasicTimer implemented using synchronization. Creates it if it does not exist. Note that if the
     * same ID already exists with a different implementation or with different stats setting, the earlier timer
     * will be returned.
     * @param id The id of the timer to assign or get.
     * @param withStats Whether to record stats or not. If true, the timer will be much slower.
     * @return The BasicTimer.
     */
    private BasicTimer doGetSyncTimer(String id, boolean withStats) {

        synchronized (timers) {

            BasicTimer timer = timers.get(id);

            if(timer == null) {

                timer = new BasicTimerSyncImpl(id, withStats);
                timers.put(id, timer);

            }

            return timer;

        }

    }

    /**
     * Get a BasicCounter implemented using atomic classes. Creates it if it does not exist. Note that if the
     * same ID already exists with a different implementation or with different stats setting, the earlier counter
     * will be returned.
     * @param id The id of the counter to assign or get.
     * @param withStats Whether to record stats or not. If true, the counter will be much slower.
     * @return The BasicCounter.
     */
    private BasicCounter doGetAtomicCounter(String id, boolean withStats) {

        synchronized (counters) {

            BasicCounter counter = counters.get(id);

            if(counter == null) {

                counter = new BasicCounterAtomicImpl(id, withStats);
                counters.put(id, counter);

            }

            return counter;

        }

    }

    /**
     * Get a BasicCounter implemented using synchronization. Creates it if it does not exist. Note that if the
     * same ID already exists with a different implementation or with different stats setting, the earlier counter
     * will be returned.
     * @param id The id of the counter to assign or get.
     * @param withStats Whether to record stats or not. If true, the counter will be much slower.
     * @return The BasicCounter.
     */
    private BasicCounter doGetSyncCounter(String id, boolean withStats) {

        synchronized (counters) {

            BasicCounter counter = counters.get(id);

            if(counter == null) {

                counter = new BasicCounterSyncImpl(id, withStats);
                counters.put(id, counter);

            }

            return counter;

        }

    }

    private String doPrettyPrint() {

        StringBuilder s = new StringBuilder(1024);

        s.append("BasicTimers:\n");

        synchronized (timers) {

            for(Object timer : timers.values()) {

                s.append("\t").append(timer.toString()).append("\n");

            }

        }

        s.append("\nBasicCounters:\n");

        synchronized (counters) {

            for(Object counter : counters.values()) {

                s.append("\t").append(counter.toString()).append("\n");

            }

        }

        return s.toString();

    }

    private synchronized void doReset() {

        timers.clear();
        counters.clear();

    }

}
