package com.goexp.galgame.data.piplline.handler;

import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;

import java.util.concurrent.BlockingQueue;

public abstract class DefaultStarter<T> extends DefaultMessageHandler<T> implements Starter {

    @Override
    public void process(Message<T> message, MessageQueueProxy<Message> msgQueue) {
        process(msgQueue);
    }

}
