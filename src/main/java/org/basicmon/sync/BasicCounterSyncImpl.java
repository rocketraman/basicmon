package org.basicmon.sync;

import org.basicmon.BasicCounter;
import org.basicmon.util.BasicCounterUtil;

/**
 * A basic counter. The val in {@link BasicMonSyncBase} will represent the counter.
 */
public final class BasicCounterSyncImpl extends BasicMonSyncBase implements BasicCounter {

    public BasicCounterSyncImpl(String id) {

        super(id, false);

    }

    public BasicCounterSyncImpl(String id, boolean withStats) {

        super(id, withStats);

    }

    @Override
    public String toString() {

        return BasicCounterUtil.toString(this);

    }

}
