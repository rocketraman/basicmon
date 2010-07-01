package com.rocketraman.basicmon.benchmark;

import com.rocketraman.basicmon.BasicMonManager;
import com.rocketraman.basicmon.BasicTimer;
import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class SimonMultiThreadedComparison {

    private static final int THREADS = 300;
    private static final int LOOP = 10000;

    private static final String NAME = SimonUtils.generateName(null, false);

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
        int round = 1;
        while (true) {
            System.out.println("\nRound: " + round++);
            long ns = System.nanoTime();
            latch = new CountDownLatch(THREADS);
            for (int i = 0; i < THREADS; i++) {
                new SimonMultithreadedStress().start();
            }
            latch.await();
            ns = System.nanoTime() - ns;
            System.out.println("Result: " + SimonManager.getStopwatch(NAME));
            System.out.println("Test Simon Total: " + SimonUtils.presentNanoTime(ns));

            ns = System.nanoTime();
            latch = new CountDownLatch(THREADS);
            for (int i = 0; i < THREADS; i++) {
                new BasicTimerMultithreadedStress().start();
            }
            latch.await();
            ns = System.nanoTime() - ns;
            System.out.println("Result: " + BasicMonManager.getTimer("basicmon-stopwatch"));
            System.out.println("Test BasicTimer Total: " + SimonUtils.presentNanoTime(ns));
        }
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

    private static class BasicTimerMultithreadedStress extends Thread {
        public void run() {
            BasicTimer stopwatch = BasicMonManager.getTimer("basicmon-stopwatch", false);
            for (int i = 0; i < LOOP; i++) {
                stopwatch.start().stop();
            }
            latch.countDown();
        }
    }
}
