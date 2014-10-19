package org.basicmon.benchmark;

import org.basicmon.BasicCounter;
import org.basicmon.BasicMonManager;
import org.javasimon.Counter;
import org.javasimon.SimonManager;
import org.javasimon.utils.SimonUtils;

import java.util.concurrent.CountDownLatch;

/**
 * Under high contention for the counter itself, we want to achieve as many counter operations per time unit
 * as possible.
 */
@SuppressWarnings("UnusedDeclaration")
public final class CounterOpsComparison {

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
         CounterOpsComparison test = new CounterOpsComparison();
         System.out.println("Threads: " + THREADS);
         int round = 1;
         while (true) {
             System.out.println("\nRound: " + round++);

             test.doBasicAtomic(THREADS, LOOP);
             BasicMonManager.reset();

             test.doBasicSync(THREADS, LOOP);
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
        Counter counter = SimonManager.getCounter(NAME);

        System.out.println("Result: " + counter);
        System.out.println("Test Simon Total: " + SimonUtils.presentNanoTime(ns));
    }

    private void doBasicSync(int threads, int loops) {
        long ns = System.nanoTime();
        latch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new BasicSyncMultithreadedStress(loops).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ns = System.nanoTime() - ns;
        BasicCounter counter = BasicMonManager.getCounter(NAME);

        System.out.println("Result: " + counter);
        System.out.println("Test Basic Sync Total: " + SimonUtils.presentNanoTime(ns));
    }

    private void doBasicAtomic(int threads, int loops) {
        long ns = System.nanoTime();
        latch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new BasicAtomicMultithreadedStress(loops).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ns = System.nanoTime() - ns;
        BasicCounter counter = BasicMonManager.getCounter(NAME);

        System.out.println("Result: " + counter);
        System.out.println("Test Basic Atomic Total: " + SimonUtils.presentNanoTime(ns));
    }

    private static class SimonMultithreadedStress extends Thread {
        int loops;
        long result;
        public SimonMultithreadedStress(int loops) {
            this.loops = loops;
        }

        public void run() {
            Counter counter = SimonManager.getCounter(NAME);
            for (int i = 0; i < loops; i++) {
                counter.increase();
            }
            latch.countDown();
        }
    }

    private static class BasicSyncMultithreadedStress extends Thread {
        int loops;
        long result;
        public BasicSyncMultithreadedStress(int loops) {
            this.loops = loops;
        }

        public void run() {
            BasicCounter counter = BasicMonManager.getSyncCounter(NAME);
            for (int i = 0; i < loops; i++) {
                counter.increment();
            }
            latch.countDown();
        }
    }

    private static class BasicAtomicMultithreadedStress extends Thread {
        int loops;
        long result;
        public BasicAtomicMultithreadedStress(int loops) {
            this.loops = loops;
        }

        public void run() {
            BasicCounter counter = BasicMonManager.getAtomicCounter(NAME);
            for (int i = 0; i < loops; i++) {
                counter.increment();
            }
            latch.countDown();
        }
    }

}
