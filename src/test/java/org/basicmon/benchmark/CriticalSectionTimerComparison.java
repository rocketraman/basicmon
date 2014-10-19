package org.basicmon.benchmark;

import org.basicmon.BasicMonManager;
import org.basicmon.BasicTimer;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

import java.util.concurrent.CountDownLatch;

/**
 * Under high contention for the timer itself, we still want the underlying code to be timed without including
 * any time waiting for timer locks, but rather timing the underlying code itself. See for example this issue I
 * found in JavaSimon: https://code.google.com/p/javasimon/issues/detail?id=21, as well as
 * https://github.com/virgo47/javasimon/issues/1. This test introduces multiple threads that are contending for
 * the same timer, but has an empty timed section. The total time reported by the timer should be as low as possible.
 */
@SuppressWarnings("UnusedDeclaration")
public final class CriticalSectionTimerComparison {

    // no contention
//    private static final int THREADS = 1;
//    private static final int LOOP = 1000000;

    // low contention
    private static final int THREADS = Runtime.getRuntime().availableProcessors();
    private static final int LOOP = 100000;

    // high contention
//    private static final int THREADS = 300;
//    private static final int LOOP = 100000;

    private static final String NAME = "CriticalSection";

    private static CountDownLatch latch;

    /**
      * Entry point of the demo application.
      *
      * @param args command line arguments
      * @throws InterruptedException should not happen
      */
     public static void main(String[] args) throws InterruptedException {
         CriticalSectionTimerComparison test = new CriticalSectionTimerComparison();
         System.out.println("Threads: " + THREADS);
         int round = 1;
         while (true) {
             System.out.println("\nRound: " + round++);

             test.doBasicSync(THREADS, LOOP);
             BasicMonManager.reset();

             test.doBasicSyncWithStats(THREADS, LOOP);
             BasicMonManager.reset();

             test.doBasicAtomic(THREADS, LOOP);
             BasicMonManager.reset();

             test.doSimon(THREADS, LOOP);
             SimonManager.clear();
         }
     }

    private void doSimon(int threads, int loops) {
        long ns = System.nanoTime();
        latch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            SimonMultithreadedStress test = new SimonMultithreadedStress(loops);
            test.start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ns = System.nanoTime() - ns;
        Stopwatch stopwatch = SimonManager.getStopwatch(NAME);
        System.out.println("Result: " + stopwatch +
            ", mean " + SimonUtils.presentNanoTime(stopwatch.getTotal() / stopwatch.getCounter()) +
            ", active max " + stopwatch.getMaxActive());
        System.out.println("Test Simon Total: " + SimonUtils.presentNanoTime(ns));
    }

    private void doBasicAtomic(int threads, int loops) {
        long ns = System.nanoTime();
        latch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new BasicAtomicTimerMultithreadedStress(loops).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ns = System.nanoTime() - ns;
        System.out.println("Result: " + BasicMonManager.getTimer(NAME));
        System.out.println("Test BasicTimer Atomic Total: " + SimonUtils.presentNanoTime(ns));
    }

    private void doBasicSync(int threads, int loops) {
        long ns = System.nanoTime();
        latch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new BasicSyncTimerMultithreadedStress(loops).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ns = System.nanoTime() - ns;
        System.out.println("Result: " + BasicMonManager.getTimer(NAME));
        System.out.println("Test BasicTimer Sync Total: " + SimonUtils.presentNanoTime(ns));
    }

    private void doBasicSyncWithStats(int threads, int loops) {
        long ns = System.nanoTime();
        latch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new BasicSyncTimerWithStatsMultithreadedStress(loops).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ns = System.nanoTime() - ns;
        System.out.println("Result: " + BasicMonManager.getTimer(NAME));
        System.out.println("Test BasicTimer Sync With Stats Total: " + SimonUtils.presentNanoTime(ns));
    }

    private static class SimonMultithreadedStress extends Thread {
        int loops;
        long result;
        public SimonMultithreadedStress(int loops) {
            this.loops = loops;
        }

        public void run() {
            Stopwatch stopwatch = SimonManager.getStopwatch(NAME);
            for (int i = 0; i < loops; i++) {
                Split split = stopwatch.start().stop();
                result |= split.runningFor();
            }
            latch.countDown();
        }
    }

    private static class BasicAtomicTimerMultithreadedStress extends Thread {
        int loops;
        long result;
        public BasicAtomicTimerMultithreadedStress(int loops) {
            this.loops = loops;
        }

        public void run() {
            BasicTimer timer = BasicMonManager.getAtomicTimer(NAME);
            for (int i = 0; i < loops; i++) {
                result |= timer.start().stop();
            }
            latch.countDown();
        }
    }

    private static class BasicSyncTimerMultithreadedStress extends Thread {
        int loops;
        long result;
        public BasicSyncTimerMultithreadedStress(int loops) {
            this.loops = loops;
        }

        public void run() {
            BasicTimer timer = BasicMonManager.getSyncTimer(NAME);
            for (int i = 0; i < loops; i++) {
                result |= timer.start().stop();
            }
            latch.countDown();
        }
    }

    private static class BasicSyncTimerWithStatsMultithreadedStress extends Thread {
        int loops;
        long result;
        public BasicSyncTimerWithStatsMultithreadedStress(int loops) {
            this.loops = loops;
        }

        public void run() {
            BasicTimer timer = BasicMonManager.getSyncTimerWithStats(NAME);
            for (int i = 0; i < loops; i++) {
                result |= timer.start().stop();
            }
            latch.countDown();
        }
    }
}
