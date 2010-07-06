package org.basicmon.sync;

import org.basicmon.BasicCounter;
import org.basicmon.BasicMonManager;
import org.basicmon.CounterTest;

public class SyncCounterTest extends CounterTest {

    @Override
    protected BasicCounter getCounter(String name) {

        return BasicMonManager.getSyncCounter(name);

    }

    @Override
    protected BasicCounter getCounterWithStats(String name) {

        return BasicMonManager.getSyncCounterWithStats(name);

    }

}
