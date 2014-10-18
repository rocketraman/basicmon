package org.basicmon.benchmark;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.api.SkipThisScenarioException;
import org.basicmon.BasicMonManager;
import org.basicmon.BasicTimer;
import org.basicmon.BasicTimerSplit;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

import static org.basicmon.benchmark.LoopComparison.*;

/**
 * Run: mvn exec:java -Dexec.classpathScope="test" -Dexec.mainClass=com.google.caliper.runner.CaliperMain -Dexec.args="--print-config --instrument runtime org.basicmon.benchmark.SimonComparison"
 */
@SuppressWarnings("UnusedDeclaration")
public class SimonComparison {

    @Param({"none", "simon", "basic"}) String type;
    @Param({"getonce", "geteach"}) String getWhen;

    @BeforeExperiment
    public void setup() throws Exception {
        BasicMonManager.reset();
        SimonManager.clear();
        LoopComparison.setup();
    }

    @Benchmark
    public long timer(int reps) {
        String type = this.type;
        String getWhen = this.getWhen;
        if("none".equals(type) && "getonce".equals(getWhen)) {
            return noTimer(reps);
        } else if("none".equals(type)) {
            throw new SkipThisScenarioException();
        } else if("simon".equals(type) && "getonce".equals(getWhen)) {
            return simonOneStopwatch(reps);
        } else if("simon".equals(type) && "geteach".equals(getWhen)) {
            return simonGetStopwatch(reps);
        } else if("basic".equals(type) && "getonce".equals(getWhen)) {
            return basicOneStopwatch(reps);
        } else if("basic".equals(type) && "geteach".equals(getWhen)) {
            return basicGetStopwatch(reps);
        } else {
            throw new IllegalArgumentException("Invalid parameter combination.");
        }
    }

    public long noTimer(int reps) {
        for(int i = 0; i < reps; i++) {
            stay();
        }
        return loopVar;
    }

    public long simonOneStopwatch(int reps) {
        final Stopwatch stopwatch = SimonManager.getStopwatch("bu");
        Call<Void> initStopwatch = new Call<Void>() {
            public Void call() {
                stopwatch.reset();
                return null;
            }
        };
        Call<Stopwatch> getStopwatch = new Call<Stopwatch>() {
            public Stopwatch call() {
                return stopwatch;
            }
        };
        testSimon(initStopwatch, getStopwatch, reps);
        return loopVar;
    }

    public long simonGetStopwatch(int reps) {
        Call<Void> initStopwatch = new Call<Void>() {
            public Void call() {
                SimonManager.getStopwatch("bu").reset();
                return null;
            }
        };
        Call<Stopwatch> getStopwatch = new Call<Stopwatch>() {
            public Stopwatch call() {
                return SimonManager.getStopwatch("bu");
            }
        };
        testSimon(initStopwatch, getStopwatch, reps);
        return loopVar;
    }

    public long basicOneStopwatch(int reps) {
        final BasicTimer timer = BasicMonManager.getTimer("bu");
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
        testBasic(initTimer, getTimer, reps);
        return loopVar;
    }

    public long basicGetStopwatch(int reps) {
        Call<Void> initTimer = new Call<Void>() {
            public Void call() {
                BasicMonManager.getTimer("bu").reset();
                return null;
            }
        };
        Call<BasicTimer> getTimer = new Call<BasicTimer>() {
            public BasicTimer call() {
                return BasicMonManager.getTimer("bu");
            }
        };
        testBasic(initTimer, getTimer, reps);
        return loopVar;
    }

    private void testSimon(Call<Void> initStopwatch, Call<Stopwatch> getStopwatch, int reps) {
        initStopwatch.call();
        for (int i = 0; i < reps; i++) {
            Split split = getStopwatch.call().start();
            stay();
            split.stop();
        }
    }

    private void testBasic(Call<Void> initTimer, Call<BasicTimer> getTimer, int reps) {
        initTimer.call();
        for (int i = 0; i < reps; i++) {
            BasicTimerSplit split = getTimer.call().start();
            stay();
            split.stop();
        }
    }

}
