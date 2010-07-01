package com.rocketraman.basicmon;

/**
 * A basic counter.
 */
public class BasicCounter extends BasicMonBase {

    protected BasicCounter(String id) {

        super(id, false);

    }

    protected BasicCounter(String id, boolean withStats) {

        super(id, withStats);

    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder(256)
            .append("BasicCounter: [").append(id).append("]")
            .append(" current ").append(getCounter());

        if(withStats) {

            s.append(", mean ").append((long)getMean())
                .append(", min ").append(getMin())
                .append(", max ").append(getMax())
                .append(", stddev ").append((long)getStdDev());

        }

        return s.toString();

    }

}
