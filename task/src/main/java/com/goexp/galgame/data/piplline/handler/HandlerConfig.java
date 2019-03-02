package com.goexp.galgame.data.piplline.handler;

import com.goexp.galgame.data.piplline.core.MessageHandler;

import java.util.concurrent.ExecutorService;

public class HandlerConfig<T> {

    public int mesType;

    public ExecutorService executor;

    public MessageHandler<T> messageHandler;


    public HandlerConfig(int mesType, MessageHandler<T> messageHandler, ExecutorService executor) {
        this.mesType = mesType;
        this.executor = executor;
        this.messageHandler = messageHandler;
    }
}
