package com.goexp.galgame.data.piplline.core;

import com.goexp.galgame.data.piplline.exception.RuntimeInterruptedException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class MessageQueueProxy<T> {
    final private BlockingQueue<T> msgQueue;

    public MessageQueueProxy(int capacity) {
        msgQueue = new ArrayBlockingQueue<>(capacity);
    }

    public boolean offer(T o) {
        try {
            return msgQueue.offer(o, 60, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeInterruptedException(e);
        }
    }

    public boolean offer(T o, long timeout, TimeUnit unit) {
        try {
            return msgQueue.offer(o, timeout, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeInterruptedException(e);
        }
    }


    public T pull() {
        try {
            return msgQueue.poll(60, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeInterruptedException(e);
        }
    }

    public T poll(long timeout, TimeUnit unit) {
        try {
            return msgQueue.poll(timeout, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeInterruptedException(e);
        }
    }
}
