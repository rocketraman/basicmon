package org.basicmon.atomic;

import org.basicmon.BasicCounter;
import org.basicmon.util.BasicCounterUtil;

/**
 * A basic counter. The val in {@link BasicMonAtomicBase} will represent the counter.
 */
public final class BasicCounterAtomicImpl extends BasicMonAtomicBase implements BasicCounter {

    public BasicCounterAtomicImpl(String id) {

        super(id, false);

    }

    public BasicCounterAtomicImpl(String id, boolean withStats) {

        super(id, withStats);

    }

    @Override
    public String toString() {

        return BasicCounterUtil.toString(this);

    }

}
