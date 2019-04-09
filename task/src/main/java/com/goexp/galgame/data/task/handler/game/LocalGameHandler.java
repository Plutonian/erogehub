package com.goexp.galgame.data.task.handler.game;

import com.goexp.galgame.data.Config;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.task.handler.MesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

public class LocalGameHandler extends DefaultMessageHandler<Integer> {

    final private Logger logger = LoggerFactory.getLogger(LocalGameHandler.class);


    @Override
    public void process(final Message<Integer> message, MessageQueueProxy<Message> msgQueue) {

        var gid = message.entity;
        logger.debug("<Game> {}", gid);

        var bytes = Map.entry(gid, Objects.requireNonNull(getContent(gid), gid.toString()));

        msgQueue.offer(new Message<>(MesType.ContentBytes, bytes));
    }

    private byte[] getContent(int id) {
        try {
            var path = Config.GAME_CACHE_ROOT.resolve(String.format("%d.bytes", id));

            return Files.readAllBytes(path);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
