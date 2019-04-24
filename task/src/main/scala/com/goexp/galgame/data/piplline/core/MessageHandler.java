package com.goexp.galgame.data.piplline.core;

public interface MessageHandler<T> {

    void process(Message<T> message, MessageQueueProxy<Message> msgQueue);

}
