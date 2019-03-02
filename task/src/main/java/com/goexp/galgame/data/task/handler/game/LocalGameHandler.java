package com.goexp.galgame.data.task.handler.game;

import com.goexp.galgame.data.db.importor.Config;
import com.goexp.galgame.data.piplline.core.MesType;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class LocalGameHandler extends DefaultMessageHandler<Integer> {

    final private Logger logger = LoggerFactory.getLogger(LocalGameHandler.class);


    @Override
    public void process(final Message<Integer> message, BlockingQueue<Message> msgQueue) {

        var gid = message.entity;
        logger.debug("<Game> {}", gid);

        var bytes = Map.entry(gid, Objects.requireNonNull(getContent(gid),gid.toString()));

        try {
            msgQueue.offer(new Message<>(MesType.ContentBytes, bytes), 60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private byte[] getContent(int id) {
        try {
            var path = Config.GAME_CACHE_ROOT.resolve(String.format("%d.bytes", id));

            if (Files.exists(path)) {
                return Files.readAllBytes(path);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
