package com.goexp.galgame.data.piplline.handler;

import com.goexp.galgame.data.piplline.core.Message;

import java.util.concurrent.BlockingQueue;

public abstract class DefaultStarter<T> extends DefaultMessageHandler<T> implements Starter {

    @Override
    public void process(Message<T> message, BlockingQueue<Message> msgQueue) {
        process(msgQueue);
    }

}
