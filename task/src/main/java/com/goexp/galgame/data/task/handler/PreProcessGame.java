package com.goexp.galgame.data.task.handler;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.data.db.importor.mongdb.GameDB;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class PreProcessGame extends DefaultMessageHandler<Game> {

    final private Logger logger = LoggerFactory.getLogger(PreProcessGame.class);

    final private GameDB importor = new GameDB();

    @Override
    public void process(final Message<Game> message, BlockingQueue<Message> msgQueue) {

        var game = message.entity;
        logger.debug("<Game> {}", game);


        if (importor.exist(game.id)) {
            logger.debug("<Update> {}", game.simpleView());
            importor.update(game);
        } else {
            game.state = GameState.UNCHECKED;
            logger.info("<Insert> {}", game.simpleView());
            importor.insert(game);
        }


        try {
            msgQueue.offer(new Message<>(MesType.NEED_DOWN_GAME, game.id), 60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
