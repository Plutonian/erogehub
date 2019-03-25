package com.goexp.galgame.data.task.handler;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.data.db.importor.mongdb.GameDB;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreProcessGame extends DefaultMessageHandler<Game> {

    final private Logger logger = LoggerFactory.getLogger(PreProcessGame.class);

    final private GameDB importor = new GameDB();

    @Override
    public void process(final Message<Game> message, MessageQueueProxy<Message> msgQueue) {

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


        msgQueue.offer(new Message<>(MesType.NEED_DOWN_GAME, game.id));

    }

}
