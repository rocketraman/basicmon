package org.basicmon.benchmark;

class LoopComparison {

    public static long loopVar = 0;
    public static final int INNER_LOOP = 1000;

    public static void setup() throws Exception {
        loopVar = 0;
    }

    public static void stay() {
        for (int j = 0; j < INNER_LOOP; j++) {
            loopVar++;
        }
    }

    public static interface Call<V> {
        V call();
    }
}
