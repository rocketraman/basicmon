package com.rocketraman.basicmon.benchmark;

import com.rocketraman.basicmon.BasicMonManager;
import com.rocketraman.basicmon.BasicTimer;
import com.rocketraman.basicmon.BasicTimerSplit;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

/**
 * Docs
 */
public class SimonComparison {

    private static final int OUTER_LOOP = 1000000;
    private static final int INNER_LOOP = 1000;

    public static long loopvar = 0;

    private SimonComparison() {
    }

    /**
     * Entry point of the demo application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        int round = 1;
        while (true) {
            System.out.println("\nRound: " + round++);
            noTimerTest();
            simonTest();
            simonTest2();
            basicTest();
            basicTest2();
            basicFastTest();
            basicFastTest2();
        }
    }

    private static void noTimerTest() {
        long ns = System.nanoTime();
        for (int i = 0; i < OUTER_LOOP; i++) {
            stay();
            stay();
        }
        ns = System.nanoTime() - ns;

        System.out.println("No timer total: " + SimonUtils.presentNanoTime(ns) +
                " real estimate: " + SimonUtils.presentNanoTime(ns / 2));
    }

    private static void simonTest() {
        Stopwatch stopwatch = SimonManager.getStopwatch("bu");
        stopwatch.reset();

        long ns = System.nanoTime();
        for (int i = 0; i < OUTER_LOOP; i++) {
            stay();
            Split split = stopwatch.start();
            stay();
            split.stop();
        }
        ns = System.nanoTime() - ns;

        printSimonResults(ns, stopwatch, "Simon start/stop");
    }

    private static void simonTest2() {
        SimonManager.getStopwatch("org.javasimon.examples.stopwatch1").reset();

        long ns = System.nanoTime();
        for (int i = 0; i < OUTER_LOOP; i++) {
            stay();
            Split split = SimonManager.getStopwatch("org.javasimon.examples.stopwatch1").start();
            stay();
            split.stop();
        }
        ns = System.nanoTime() - ns;

        Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.examples.stopwatch1");
        printSimonResults(ns, stopwatch, "Simon get+start/stop");
    }

    private static void printSimonResults(long ns, Stopwatch stopwatch, String title) {
        System.out.println(title + " count: " + stopwatch.getCounter() + ", total: " + SimonUtils.presentNanoTime(stopwatch.getTotal()) +
            ", max: " + SimonUtils.presentNanoTime(stopwatch.getMax()) + ", min: " + SimonUtils.presentNanoTime(stopwatch.getMin()) +
            ", real: " + SimonUtils.presentNanoTime(ns));
    }

    private static void basicTest() {
        BasicTimer stopwatch = BasicMonManager.getTimer("bu");
        stopwatch.reset();

        long ns = System.nanoTime();
        for (int i = 0; i < OUTER_LOOP; i++) {
            BasicTimerSplit split = stopwatch.start();
            stay();
            split.stop();
            stay();
        }
        ns = System.nanoTime() - ns;

        printBasicTimerResults(ns, stopwatch, "BasicTimer start/stop");
    }

    private static void basicTest2() {
        BasicMonManager.getTimer("BasicTimer.stopwatch1").reset();

        long ns = System.nanoTime();
        for (int i = 0; i < OUTER_LOOP; i++) {
            stay();
            BasicTimerSplit split = BasicMonManager.getTimer("BasicTimer.stopwatch1").start();
            stay();
            split.stop();
        }
        ns = System.nanoTime() - ns;

        BasicTimer stopwatch = BasicMonManager.getTimer("BasicTimer.stopwatch1");
        printBasicTimerResults(ns, stopwatch, "BasicTimer get+start/stop");
    }

    private static void basicFastTest() {
        BasicTimer stopwatch = BasicMonManager.getTimer("bu", true);
        stopwatch.reset();

        long ns = System.nanoTime();
        for (int i = 0; i < OUTER_LOOP; i++) {
            BasicTimerSplit split = stopwatch.start();
            stay();
            split.stop();
            stay();
        }
        ns = System.nanoTime() - ns;

        printBasicTimerResults(ns, stopwatch, "BasicTimer fast start/stop");
    }

    private static void basicFastTest2() {
        BasicMonManager.getTimer("BasicTimer.fastStopwatch", true).reset();

        long ns = System.nanoTime();
        for (int i = 0; i < OUTER_LOOP; i++) {
            stay();
            BasicTimerSplit split = BasicMonManager.getTimer("BasicTimer.fastStopwatch").start();
            stay();
            split.stop();
        }
        ns = System.nanoTime() - ns;

        BasicTimer stopwatch = BasicMonManager.getTimer("BasicTimer.fastStopwatch");
        printBasicTimerResults(ns, stopwatch, "BasicTimer fast get+start/stop");
    }

    private static void printBasicTimerResults(long ns, BasicTimer stopwatch, String title) {
        System.out.println(title + " count: " + stopwatch.getCounter() + ", total: " + SimonUtils.presentNanoTime(stopwatch.getTotal()) +
            ", max: " + SimonUtils.presentNanoTime(stopwatch.getMax()) + ", min: " + SimonUtils.presentNanoTime(stopwatch.getMin()) +
            ", real: " + SimonUtils.presentNanoTime(ns));
    }

    private static void stay() {
        for (int j = 0; j < INNER_LOOP; j++) {
            loopvar++;
        }
    }

}
