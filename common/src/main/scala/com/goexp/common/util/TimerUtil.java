package com.goexp.common.util;

import java.util.concurrent.ScheduledExecutorService;

import static java.lang.System.out;

public class TimerUtil {

    private static TimerUtil defaults = new TimerUtil();
    private long old;
    private ScheduledExecutorService pool;

    public static TimerUtil getInstance() {
        return defaults;
    }

    public void start() {
        old = System.currentTimeMillis();
//        pool = Executors.newScheduledThreadPool(1);
//        pool.scheduleAtFixedRate(() -> {
//            out.print("-");
//        }, 1, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        var nextNow = System.currentTimeMillis();
//        pool.shutdownNow();

        out.format("\nUse %d ms\n", nextNow - old);
    }
}
