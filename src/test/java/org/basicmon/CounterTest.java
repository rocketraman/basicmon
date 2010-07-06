package org.basicmon;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class CounterTest {

    private static final String COUNTER_NAME = "basicmon-test-counter";

    protected abstract BasicCounter getCounter(String name);

    protected abstract BasicCounter getCounterWithStats(String name);

    @Before
    public void reset() {

        BasicMonManager.reset();

    }

    @Test
    public void basicCounterTest() {

        BasicCounter counter = getCounter(COUNTER_NAME);

        Assert.assertEquals(0, counter.getUpdateCount());
        Assert.assertEquals(-1, counter.getMax());
        Assert.assertEquals(-1, counter.getMin());
        Assert.assertEquals(Double.NaN, counter.getMean(), 0);

        counter.increment();
        Assert.assertEquals(1, counter.getVal());
        Assert.assertEquals(1, counter.getUpdateCount());
        Assert.assertEquals(1, counter.getMean(), 0);

        counter.decrement();
        Assert.assertEquals(0, counter.getVal());
        Assert.assertEquals(2, counter.getUpdateCount());
        Assert.assertEquals(0.5, counter.getMean(), 0);

    }

    @Test
    public void basicCounterTestWithStats() {

        BasicCounter counter = getCounterWithStats(COUNTER_NAME);

        Assert.assertEquals(0, counter.getUpdateCount());
        Assert.assertEquals(0, counter.getMax());
        Assert.assertEquals(0, counter.getMin());
        Assert.assertEquals(Double.NaN, counter.getMean(), 0);

        counter.increment();
        Assert.assertEquals(1, counter.getMin(), 0);    // first value is 1
        Assert.assertEquals(1, counter.getMax(), 0);

        counter.decrement();
        Assert.assertEquals(0, counter.getMin(), 0);
        Assert.assertEquals(1, counter.getMax(), 0);

    }

    @Test
    public void resetTest() {

        BasicCounter counter = getCounterWithStats(COUNTER_NAME);

        counter.setVal(20);
        Assert.assertEquals(20, counter.getVal());
        Assert.assertEquals(1, counter.getUpdateCount());

        counter.reset();

        Assert.assertEquals(0, counter.getVal());
        Assert.assertEquals(0, counter.getUpdateCount());

    }

    @Test
    public void arbitraryStartValue() {

        BasicCounter counter = getCounter(COUNTER_NAME);

        counter.setVal(47);

        Assert.assertEquals(47, counter.getVal());
        Assert.assertEquals(1, counter.getUpdateCount());
        Assert.assertEquals(47, counter.getMean(), 0);

    }

    @Test
    public void testMean() {

        BasicCounter counter = getCounter(COUNTER_NAME);

        counter.setVal(10);
        counter.setVal(15);
        counter.setVal(20);
        Assert.assertEquals(45, counter.getTotal());
        Assert.assertEquals(3, counter.getUpdateCount());
        Assert.assertEquals(15, counter.getMean(), 0);

    }

    @Test
    public void testStdDev() {

        BasicCounter counter = getCounterWithStats(COUNTER_NAME);

        counter.setVal(10);
        counter.setVal(15);
        counter.setVal(20);
        Assert.assertEquals(45, counter.getTotal());
        Assert.assertEquals(3, counter.getUpdateCount());
        Assert.assertEquals(5, counter.getStdDev(), 0);

    }

}
