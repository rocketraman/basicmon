package org.basicmon.atomic;

import org.basicmon.BasicCounter;
import org.basicmon.BasicMonManager;
import org.basicmon.CounterTest;

public class AtomicCounterTest extends CounterTest {

    @Override
    protected BasicCounter getCounter(String name) {

        return BasicMonManager.getAtomicCounter(name);

    }

    @Override
    protected BasicCounter getCounterWithStats(String name) {

        return BasicMonManager.getAtomicCounterWithStats(name);

    }

}