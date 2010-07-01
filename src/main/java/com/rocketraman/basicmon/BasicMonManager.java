package com.rocketraman.basicmon;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A manager for BasicTimer and BasicCounter instances. BasicTimer is used to provide some (basic) timing information during
 * testing and debugging. BasicCounter provides count information, with associated stats.
 */
public class BasicMonManager {

    private static final SortedMap<String,BasicTimer> timers = new TreeMap<String, BasicTimer>();
    private static final SortedMap<String,BasicCounter> counters = new TreeMap<String, BasicCounter>();
    private static final ReadWriteLock timersLock = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock countersLock = new ReentrantReadWriteLock(false);

    public static BasicTimer getTimer(String id) {

        return getTimer(id, false);

    }

    public static BasicTimer getTimer(String id, boolean withStats) {

        timersLock.readLock().lock();

        BasicTimer timer = timers.get(id);

        if(timer == null) {

            timersLock.readLock().unlock();
            timersLock.writeLock().lock();

            timer = timers.get(id);

            if(timer == null) {

                timer = new BasicTimer(id, withStats);
                timers.put(id, timer);

            }

            timersLock.writeLock().unlock();
            return timer;

        }

        timersLock.readLock().unlock();

        return timer;

    }

    public static BasicCounter getCounter(String id) {

        countersLock.readLock().lock();

        BasicCounter counter = counters.get(id);

        if(counter == null) {

            countersLock.readLock().unlock();
            countersLock.writeLock().lock();

            counter = counters.get(id);

            if(counter == null) {

                counter = new BasicCounter(id);
                counters.put(id, counter);

            }

            countersLock.writeLock().unlock();
            return counter;

        }

        countersLock.readLock().unlock();

        return counter;

    }

    public static void reset() {

        timersLock.writeLock().lock();
        timers.clear();
        timersLock.writeLock().unlock();

        countersLock.writeLock().lock();
        counters.clear();
        countersLock.writeLock().unlock();

    }

    public static String prettyPrint() {

        StringBuilder s = new StringBuilder(1024);

        s.append("BasicTimers:\n");

        timersLock.readLock().lock();

        for(Object timer : timers.values()) {

            s.append("\t").append(timer.toString()).append("\n");

        }

        timersLock.readLock().unlock();

        s.append("\nBasicCounters:\n");

        countersLock.readLock().lock();

        for(Object counter : counters.values()) {

            s.append("\t").append(counter.toString()).append("\n");

        }

        countersLock.readLock().unlock();

        return s.toString();

    }

}
