package org.basicmon;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class TimerTest {

    private static final String TIMER_NAME = "basicmon-test-timer";

    protected abstract BasicTimer getTimer(String name);

    protected abstract BasicTimer getTimerWithStats(String name);

    @Before
    public void reset() {

        BasicMonManager.reset();

    }

    @Test
    public void basicTimerTest() {

        BasicTimer timer = getTimer(TIMER_NAME);

        long split = timer.start().stop();

        Assert.assertTrue(timer.getTotal() >= 0);
        Assert.assertEquals(split, timer.getTotal());
        Assert.assertEquals(1, timer.getUpdateCount());
        Assert.assertEquals(timer.getTotal(), timer.getMean(), 0);
        Assert.assertEquals(-1, timer.getMax());
        Assert.assertEquals(-1, timer.getMin());
        Assert.assertEquals(0, timer.getActive(), 0);
        Assert.assertEquals(1, timer.getActiveMean(), 0);

    }

    @Test
    public void basicTimerTestWithStats() {

        BasicTimer timer = getTimerWithStats(TIMER_NAME);

        long split = timer.start().stop();

        Assert.assertTrue(timer.getTotal() >= 0);
        Assert.assertEquals(timer.getTotal(), timer.getMax());
        Assert.assertEquals(timer.getTotal(), timer.getMin());
        Assert.assertEquals(0, timer.getStdDev(), 0);
        Assert.assertEquals(1, timer.getActiveMax());
        Assert.assertEquals(0, timer.getActiveVariance(), 0);
        Assert.assertEquals(0, timer.getActiveStdDev(), 0);

    }

    @Test
    public void resetTest() {

        BasicTimer timer = getTimer(TIMER_NAME);

        timer.addTime(100);
        Assert.assertEquals(100, timer.getTotal());
        Assert.assertEquals(100, timer.getMean(), 0);
        Assert.assertEquals(1, timer.getUpdateCount());

        timer.reset();
        Assert.assertEquals(0, timer.getTotal(), 0);
        Assert.assertEquals(Double.NaN, timer.getMean(), 0);
        Assert.assertEquals(0, timer.getUpdateCount());

    }

    @Test
    public void testMean() {

        BasicTimer timer = getTimer(TIMER_NAME);

        timer.addTime(10);
        timer.addTime(15);
        timer.addTime(20);
        Assert.assertEquals(45, timer.getTotal());
        Assert.assertEquals(3, timer.getUpdateCount());
        Assert.assertEquals(15, timer.getMean(), 0);

    }

    @Test
    public void testStdDev() {

        BasicTimer timer = getTimerWithStats(TIMER_NAME);

        timer.addTime(10);
        timer.addTime(15);
        timer.addTime(20);
        Assert.assertEquals(45, timer.getTotal());
        Assert.assertEquals(3, timer.getUpdateCount());
        Assert.assertEquals(5, timer.getStdDev(), 0);

    }

}
