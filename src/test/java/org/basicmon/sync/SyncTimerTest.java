package org.basicmon.sync;

import org.basicmon.BasicMonManager;
import org.basicmon.BasicTimer;
import org.basicmon.TimerTest;

public class SyncTimerTest extends TimerTest {

    @Override
    protected BasicTimer getTimer(String name) {

        return BasicMonManager.getSyncTimer(name);

    }

    @Override
    protected BasicTimer getTimerWithStats(String name) {

        return BasicMonManager.getSyncTimerWithStats(name);

    }

}
