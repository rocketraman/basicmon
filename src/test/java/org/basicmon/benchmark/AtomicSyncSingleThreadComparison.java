package org.basicmon.benchmark;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import org.basicmon.BasicMonManager;
import org.basicmon.BasicTimer;
import org.basicmon.BasicTimerSplit;

import static org.basicmon.benchmark.LoopComparison.*;

/**
 * Run: mvn exec:java -Dexec.classpathScope="test" -Dexec.mainClass=com.google.caliper.runner.CaliperMain -Dexec.args="--print-config --instrument runtime org.basicmon.benchmark.AtomicSyncSingleThreadComparison"
 */
@SuppressWarnings("UnusedDeclaration")
public class AtomicSyncSingleThreadComparison {

    @BeforeExperiment
    public void setup() throws Exception {
        BasicMonManager.reset();
        LoopComparison.setup();
    }

    @Benchmark
    public long atomicTimer(int reps) {
        final BasicTimer timer = BasicMonManager.getAtomicTimer("bu");
        Call<Void> initTimer = new Call<Void>() {
            public Void call() {
                timer.reset();
                return null;
            }
        };
        Call<BasicTimer> getTimer = new Call<BasicTimer>() {
            public BasicTimer call() {
                return timer;
            }
        };
        test(initTimer, getTimer, reps);
        return loopVar;
    }

    @Benchmark
    public long syncTimer(int reps) {
        final BasicTimer timer = BasicMonManager.getSyncTimer("bu");
        Call<Void> initTimer = new Call<Void>() {
            public Void call() {
                timer.reset();
                return null;
            }
        };
        Call<BasicTimer> getTimer = new Call<BasicTimer>() {
            public BasicTimer call() {
                return timer;
            }
        };
        test(initTimer, getTimer, reps);
        return loopVar;
    }

    private void test(Call<Void> initTimer, Call<BasicTimer> getTimer, int reps) {
        initTimer.call();
        for (int i = 0; i < reps; i++) {
            BasicTimerSplit split = getTimer.call().start();
            stay();
            split.stop();
        }
    }

}
