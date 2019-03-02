package com.goexp.galgame.data.piplline.core;

import java.util.concurrent.BlockingQueue;

public interface MessageHandler<T> {

    void process(final Message<T> message, BlockingQueue<Message> msgQueue);

}
