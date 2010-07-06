package org.basicmon.atomic;

import org.basicmon.BasicMonManager;
import org.basicmon.BasicTimer;
import org.basicmon.TimerTest;

public class AtomicTimerTest extends TimerTest {

    @Override
    protected BasicTimer getTimer(String name) {

        return BasicMonManager.getAtomicTimer(name);

    }

    @Override
    protected BasicTimer getTimerWithStats(String name) {

        return BasicMonManager.getAtomicTimerWithStats(name);

    }

}