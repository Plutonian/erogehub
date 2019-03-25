package com.goexp.galgame.data.piplline.handler;

import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;

import java.util.concurrent.BlockingQueue;

public interface Starter {
    void process(MessageQueueProxy<Message> msgQueue);
}
