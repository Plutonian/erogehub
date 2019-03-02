package com.goexp.galgame.data.task.handler.game;

import com.goexp.galgame.data.db.importor.mysql.GameCharDB;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.db.query.GameCharService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class ProcessGameCharOK extends DefaultMessageHandler<Game> {

    final private GameCharDB gameCharService = new GameCharDB();
    final private GameCharService gameCharQuery = new GameCharService();

    final private Logger logger = LoggerFactory.getLogger(ProcessGameCharOK.class);

    @Override
    public void process(final Message<Game> message, BlockingQueue<Message> msgQueue) {

        var game = message.entity;

        game.gameCharacterList.forEach(gameCharacter -> {
            if (!gameCharService.exist(gameCharacter.id)) {
                gameCharService.insert(gameCharacter);
                logger.info("Insert {}", gameCharacter);
            } else {

                var localGameChar = gameCharQuery.get(gameCharacter.id);

                logger.debug("Update {}", gameCharacter);

                if (localGameChar.cv == null || localGameChar.cv.isEmpty()) {
                    gameCharService.updateALL(gameCharacter);
                } else {
                    gameCharService.updateALLWithoutCV(gameCharacter);
                }
            }

        });

    }

}
