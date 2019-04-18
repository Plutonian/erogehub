package com.goexp.galgame.data.piplline.core;

public interface MessageHandler<T> {

    void process(final Message<T> message, MessageQueueProxy<Message> msgQueue);

}
