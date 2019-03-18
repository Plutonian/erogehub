package com.goexp.galgame.data.task.handler.game;

import com.goexp.galgame.data.parser.ParseException;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.task.client.GetChu;
import com.goexp.galgame.data.task.handler.MesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Html2GameOK extends DefaultMessageHandler<Map.Entry<Integer, String>> {

    final private Logger logger = LoggerFactory.getLogger(Html2GameOK.class);

    @Override
    public void process(final Message<Map.Entry<Integer, String>> message, BlockingQueue<Message> msgQueue) {

        var entry = message.entity;
        var html = entry.getValue();
        var gameId = entry.getKey();

        logger.debug("<Html2GameOK> {}", gameId);

        try {
            var game = GetChu.GameService.getFrom(gameId, html);
            msgQueue.offer(new Message<>(MesType.GAME_OK, game), 60, TimeUnit.SECONDS);

        } catch (InterruptedException | ParseException e) {
            e.printStackTrace();
        }
    }

}
