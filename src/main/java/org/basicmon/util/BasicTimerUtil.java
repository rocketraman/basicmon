package org.basicmon.util;

import org.basicmon.BasicTimer;

/**
 * BasicTimer utilities.
 */
public class BasicTimerUtil {

    public static String toString(BasicTimer timer) {

        StringBuilder s = new StringBuilder(256)
            .append("BasicTimer: [").append(timer.getId()).append("]")
            .append(" total ").append(BasicMonUtil.presentNanoTime(timer.getTotal()))
            .append(", timer ").append(timer.getUpdateCount())
            .append(", mean ").append(BasicMonUtil.presentNanoTime((long)timer.getMean()));

        if(timer.isWithStats()) {

            s.append(", min ").append(BasicMonUtil.presentNanoTime(timer.getMin()))
                .append(", max ").append(BasicMonUtil.presentNanoTime(timer.getMax()))
                .append(", stddev ").append(BasicMonUtil.presentNanoTime((long)timer.getStdDev()));

        }

        s.append(", active ").append(timer.getActive())
            .append(", active mean ").append(BasicMonUtil.formatDecimal(timer.getActiveMean()));

        if(timer.isWithStats()) {

            s.append(", active max ").append(timer.getActiveMax())
                .append(", active stddev ").append(BasicMonUtil.formatDecimal(timer.getActiveStdDev()));

        }

        return s.toString();

    }

}