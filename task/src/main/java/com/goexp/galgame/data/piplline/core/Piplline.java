package com.goexp.galgame.data.piplline.core;

import com.goexp.galgame.data.piplline.exception.RuntimeInterruptedException;
import com.goexp.galgame.data.piplline.handler.HandlerConfig;
import com.goexp.galgame.data.piplline.handler.Starter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Piplline {
    final private Logger logger = LoggerFactory.getLogger(Piplline.class);

    final private MessageQueueProxy<Message> msgQueueProxy;


    final private ExecutorService listenerExecutorService = Executors.newSingleThreadExecutor();

    final private Set<HandlerConfig<?>> configs = new HashSet<>();

    final private Starter starter;


    public Piplline(Starter starter) {
        this.starter = starter;

        msgQueueProxy = new MessageQueueProxy<>(1000);
    }

    public Piplline registry(HandlerConfig config) {
        configs.add(config);
        return this;
    }


    private Piplline registryMessageHandler(int mesType, MessageHandler<?> messageHandler, ExecutorService executor) {
        configs.add(new HandlerConfig<>(mesType, messageHandler, executor));
        return this;
    }

    private Piplline registryMessageHandler(int mesType, MessageHandler<?> messageHandler, int threadCount) {
        configs.add(new HandlerConfig<>(mesType, messageHandler, Executors.newFixedThreadPool(threadCount)));
        return this;
    }


    public Piplline registryCPUTypeMessageHandler(int handleMesType, MessageHandler<?> messageHandler) {
        return registryMessageHandler(handleMesType, messageHandler, 2);
    }

    public Piplline registryCPUTypeMessageHandler(int handleMesType, MessageHandler<?> messageHandler, int threadCount) {
        return registryMessageHandler(handleMesType, messageHandler, threadCount);
    }

    public Piplline registryCPUTypeMessageHandler(int handleMesType, MessageHandler<?> messageHandler, ExecutorService executor) {
        return registryMessageHandler(handleMesType, messageHandler, executor);
    }

    public Piplline registryIOTypeMessageHandler(int handleMesType, MessageHandler<?> messageHandler) {
        return registryMessageHandler(handleMesType, messageHandler, 30);
    }

    public Piplline registryIOTypeMessageHandler(int handleMesType, MessageHandler<?> messageHandler, int threadCount) {
        return registryMessageHandler(handleMesType, messageHandler, threadCount);
    }

    public Piplline registryIOTypeMessageHandler(int handleMesType, MessageHandler<?> messageHandler, ExecutorService executor) {
        return registryMessageHandler(handleMesType, messageHandler, executor);
    }

    public void start() {

        var mesTypeMap = configs.stream()
                .collect(Collectors.groupingBy(HandlerConfig::mesType));

        listenerExecutorService.execute(() -> {

            var running = true;

            while (running) {
                try {
                    final var mes = msgQueueProxy.poll(5, TimeUnit.MINUTES);

                    if (mes != null) {
                        final var configs = mesTypeMap.get(mes.code);

                        if (configs != null)
                            for (var c : configs)
                                c.executor().execute(() -> {
                                    try {
                                        c.messageHandler().process(mes, msgQueueProxy);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });

                    } else {
                        logger.info("listener task time out!!!");
                        running = false;

                        for (var config : configs) {
                            config.executor().shutdown();
                        }
                        listenerExecutorService.shutdown();
                    }
                } catch (RuntimeInterruptedException e) {
                    e.printStackTrace();
                    running = false;
                }


            }
        });

        starter.process(msgQueueProxy);

    }
}
