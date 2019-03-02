package com.goexp.galgame.data.piplline.handler;

import com.goexp.galgame.data.piplline.core.Message;

import java.util.concurrent.BlockingQueue;

public interface Starter {
    void process(BlockingQueue<Message> msgQueue);
}
