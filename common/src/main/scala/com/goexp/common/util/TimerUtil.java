package com.goexp.common.util;

import static java.lang.System.out;

public class TimerUtil {

    private static TimerUtil defaults = new TimerUtil();
    private long old;

    public static TimerUtil getInstance() {
        return defaults;
    }

    private TimerUtil() {

    }

    public void start() {
        old = System.currentTimeMillis();
    }

    public void stop() {
        var nextNow = System.currentTimeMillis();
//        pool.shutdownNow();

        out.format("\nUse %d ms\n", nextNow - old);
    }
}
