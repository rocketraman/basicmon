package org.basicmon.benchmark;

import org.basicmon.BasicMonManager;
import org.basicmon.BasicTimer;
import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

import java.util.concurrent.CountDownLatch;

/**
 * Compares multi-threaded performance with javasimon. In this test the Atomic timers generally seem to perform
 * better than the synchronization timers, at least on Linux 2.6.23.
 */
public class SimonMultiThreadedComparison {

    // low contention
//    private static final int THREADS = Runtime.getRuntime().availableProcessors();
//    private static final int LOOP = 1000000;

    // high contention
    private static final int THREADS = 300;
    private static final int LOOP = 10000;

    private static final String NAME = SimonUtils.generateName();

    private static CountDownLatch latch;

    private SimonMultiThreadedComparison() {
    }

    /**
     * Entry point of the demo application.
     *
     * @param args command line arguments
     * @throws InterruptedException should not happen
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Threads: " + THREADS);
        int round = 1;
        while (true) {
            System.out.println("\nRound: " + round++);
            doBasicAtomic();
            BasicMonManager.reset();

            doBasicSync();

            doSimon();

            BasicMonManager.reset();
            SimonManager.clear();
        }
    }

    private static void doSimon() throws InterruptedException {
        long ns = System.nanoTime();
        latch = new CountDownLatch(THREADS);
        for (int i = 0; i < THREADS; i++) {
            new SimonMultithreadedStress().start();
        }
        latch.await();
        ns = System.nanoTime() - ns;
        Stopwatch stopwatch = SimonManager.getStopwatch(NAME);
        System.out.println("Result: " + stopwatch +
            ", mean " + SimonUtils.presentNanoTime(stopwatch.getTotal() / stopwatch.getCounter()) +
            ", active max " + stopwatch.getMaxActive());
        System.out.println("Test Simon Total: " + SimonUtils.presentNanoTime(ns));
    }

    private static void doBasicAtomic() throws InterruptedException {
        long ns = System.nanoTime();
        latch = new CountDownLatch(THREADS);
        for (int i = 0; i < THREADS; i++) {
            new BasicAtomicTimerMultithreadedStress().start();
        }
        latch.await();
        ns = System.nanoTime() - ns;
        System.out.println("Result: " + BasicMonManager.getTimer(NAME));
        System.out.println("Test BasicTimer Atomic Total: " + SimonUtils.presentNanoTime(ns));
    }

    private static void doBasicSync() throws InterruptedException {
        long ns = System.nanoTime();
        latch = new CountDownLatch(THREADS);
        for (int i = 0; i < THREADS; i++) {
            new BasicSyncTimerMultithreadedStress().start();
        }
        latch.await();
        ns = System.nanoTime() - ns;
        System.out.println("Result: " + BasicMonManager.getTimer(NAME));
        System.out.println("Test BasicTimer Sync Total: " + SimonUtils.presentNanoTime(ns));
    }

    private static class SimonMultithreadedStress extends Thread {
        public void run() {
            Stopwatch stopwatch = SimonManager.getStopwatch(NAME);
            for (int i = 0; i < LOOP; i++) {
                stopwatch.start().stop();
            }
            latch.countDown();
        }
    }

    private static class BasicAtomicTimerMultithreadedStress extends Thread {
        public void run() {
            BasicTimer timer = BasicMonManager.getAtomicTimer(NAME);
            for (int i = 0; i < LOOP; i++) {
                timer.start().stop();
            }
            latch.countDown();
        }
    }

    private static class BasicSyncTimerMultithreadedStress extends Thread {
        public void run() {
            BasicTimer timer = BasicMonManager.getTimer(NAME);
            for (int i = 0; i < LOOP; i++) {
                timer.start().stop();
            }
            latch.countDown();
        }
    }
}
