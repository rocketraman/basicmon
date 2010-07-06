package org.basicmon.util;

import org.basicmon.BasicCounter;

/**
 * BasicCounter utilities.
 */
public class BasicCounterUtil {

    public static String toString(BasicCounter counter) {

        StringBuilder s = new StringBuilder(256)
            .append("BasicCounter: [").append(counter.getId()).append("]")
            .append(" current ").append(counter.getVal())
            .append(", mean ").append((long)counter.getMean());

        if(counter.isWithStats()) {

            s.append(", min ").append(counter.getMin())
                .append(", max ").append(counter.getMax())
                .append(", stddev ").append((long)counter.getStdDev());

        }

        return s.toString();

    }

}
