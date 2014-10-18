package org.basicmon.benchmark;

import com.google.caliper.Param;
import com.google.caliper.api.AfterRep;
import com.google.caliper.api.Macrobenchmark;
import org.basicmon.BasicMonManager;
import org.basicmon.BasicTimer;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

import java.util.concurrent.CountDownLatch;

/**
 * Compares multi-threaded performance with javasimon. We compare both atomic and synchronization-based timers.
 *
 * Run: mvn exec:java -Dexec.classpathScope="test" -Dexec.mainClass=com.google.caliper.runner.CaliperMain -Dexec.args="--print-config --instrument runtime org.basicmon.benchmark.SimonMultiThreadedComparison"
 */
@SuppressWarnings("UnusedDeclaration")
public final class SimonMultiThreadedComparison {

    @Param({"8", "100"}) int threads;
    @Param({"100", "1000"}) int loops;

    private static final String NAME = SimonUtils.generateName();

    private static CountDownLatch latch;

    @AfterRep
    public void tearDown() throws Exception {
        BasicMonManager.reset();
        SimonManager.clear();
    }

    @Macrobenchmark
    public void basicAtomic() {
        int threads = this.threads;
        int loops = this.loops;
        doBasicAtomic(threads, loops);
    }

    @Macrobenchmark
    public void basicSynchronized() {
        int threads = this.threads;
        int loops = this.loops;
        doBasicSync(threads, loops);
    }

    @Macrobenchmark
    public void simon() {
        int threads = this.threads;
        int loops = this.loops;
        doSimon(threads, loops);
    }

    private void doSimon(int threads, int loops) {
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
    }

    private void doBasicAtomic(int threads, int loops) {
        latch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new BasicAtomicTimerMultithreadedStress(loops).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void doBasicSync(int threads, int loops) {
        latch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new BasicSyncTimerMultithreadedStress(loops).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
}
