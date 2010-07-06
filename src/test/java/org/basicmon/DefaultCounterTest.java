package org.basicmon;

public class DefaultCounterTest extends CounterTest {

    @Override
    protected BasicCounter getCounter(String name) {

        return BasicMonManager.getCounter(name);

    }

    @Override
    protected BasicCounter getCounterWithStats(String name) {

        return BasicMonManager.getCounterWithStats(name);

    }

}
