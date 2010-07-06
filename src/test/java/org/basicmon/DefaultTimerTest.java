package org.basicmon;

public class DefaultTimerTest extends TimerTest {

    @Override
    protected BasicTimer getTimer(String name) {

        return BasicMonManager.getTimer(name);

    }

    @Override
    protected BasicTimer getTimerWithStats(String name) {

        return BasicMonManager.getTimerWithStats(name);

    }

}
