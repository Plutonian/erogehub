package com.goexp.galgame.data.task.handler.game;

import com.goexp.galgame.data.db.importor.mongdb.GameDB;
import com.goexp.galgame.data.db.query.mongdb.GameQuery;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;

public class ProcessGameOK extends DefaultMessageHandler<Game> {

    final private Logger logger = LoggerFactory.getLogger(ProcessGameOK.class);

    final private GameQuery gameService = new GameQuery();

    final private GameDB gameDB = new GameDB();

    @Override
    public void process(final Message<Game> message, BlockingQueue<Message> msgQueue) {

        var remoteGame = message.entity;

        logger.debug("Process {}", remoteGame);

        var localGame = gameService.get(remoteGame.id);
        if (!Objects.equals(localGame, remoteGame)) {
            logger.debug("\nOld:{}\nNew:{}\n", localGame, remoteGame);
            gameDB.updateAll(remoteGame);
        }

        if (localGame.gameCharacterList != null)
            localGame.gameCharacterList.stream().forEach(g -> {
                var index = remoteGame.gameCharacterList.indexOf(g);
                var tgame = remoteGame.gameCharacterList.get(index);
                tgame.trueCV = g.trueCV;

                if (g.cv != null && !g.cv.isEmpty() && tgame.cv == null || tgame.cv.isEmpty()) {
                    tgame.cv = g.cv;
                }

            });

        gameDB.updateChar(remoteGame);

        if (localGame.imgList == null || remoteGame.imgList.size() > localGame.imgList.size()) {
            gameDB.updateImg(remoteGame);
        }


    }

}
