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

    final private MessageQueueProxy<Message<?>> msgQueueProxy;


    final private ExecutorService listenerExecutorService = Executors.newSingleThreadExecutor();

    final private Set<HandlerConfig> configs = new HashSet<>();

    final private Starter starter;


    public Piplline(Starter starter) {
        this.starter = starter;

        msgQueueProxy = new MessageQueueProxy<>(1000);
    }

    public Piplline registry(HandlerConfig config) {
        configs.add(config);
        return this;
    }


    private Piplline registry(int mesType, MessageHandler handler, ExecutorService executor) {
        configs.add(new HandlerConfig(mesType, handler, executor));
        return this;
    }

    private Piplline registry(int mesType, MessageHandler handler, int threadCount) {
        configs.add(new HandlerConfig<>(mesType, handler, Executors.newFixedThreadPool(threadCount)));
        return this;
    }


    public Piplline regForCPUType(int mesCode, MessageHandler handler) {
        return registry(mesCode, handler, 2);
    }

    public Piplline regForCPUType(int mesCode, MessageHandler handler, int threadCount) {
        return registry(mesCode, handler, threadCount);
    }

    public Piplline regForCPUType(int mesCode, MessageHandler handler, ExecutorService executor) {
        return registry(mesCode, handler, executor);
    }

    public Piplline regForIOType(int mesCode, MessageHandler handler) {
        return registry(mesCode, handler, 30);
    }

    public Piplline regForIOType(int mesCode, MessageHandler handler, int threadCount) {
        return registry(mesCode, handler, threadCount);
    }

    public Piplline regForIOType(int mesCode, MessageHandler handler, ExecutorService executor) {
        return registry(mesCode, handler, executor);
    }

    public void start() {
        starter.setQueue(msgQueueProxy);

        var mesTypeMap = configs.stream().peek(c -> c.handler().setQueue(msgQueueProxy))
                .collect(Collectors.groupingBy(HandlerConfig::mesCode));

        listenerExecutorService.execute(() -> {

            var running = true;

            while (running) {
                try {
                    final var mes = msgQueueProxy.poll(5, TimeUnit.MINUTES);

                    if (mes != null) {
                        final var configs = mesTypeMap.get(mes.code);

                        if (configs != null)
                            for (var c : configs) {
                                c.executor().execute(() -> {
                                    try {
                                        c.handler().process(mes);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }

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

        starter.process();

    }
}
