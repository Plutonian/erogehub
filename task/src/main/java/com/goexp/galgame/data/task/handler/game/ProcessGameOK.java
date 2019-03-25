package com.goexp.galgame.data.task.handler.game;

import com.goexp.common.util.Strings;
import com.goexp.galgame.data.db.importor.mongdb.GameDB;
import com.goexp.galgame.data.db.query.mongdb.GameQuery;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class ProcessGameOK extends DefaultMessageHandler<Game> {

    final private Logger logger = LoggerFactory.getLogger(ProcessGameOK.class);

    final private GameDB gameDB = new GameDB();

    private List<Game.GameCharacter> merge(List<Game.GameCharacter> local, List<Game.GameCharacter> remote) {

        if (local == null && remote == null) {
            return null;
        }

        if (local == null) {
            return remote;
        }

        // make local cache
        var localMap = local.stream().collect(Collectors.toUnmodifiableMap(cc -> cc, cc -> cc));

        //merge local to remote
        return remote.stream()
                .peek(rc -> {
                    var localC = localMap.get(rc);
                    if (localC != null) {

                        //set local truecv to remote
                        if (!Strings.isEmpty(localC.trueCV)) {
                            logger.debug("Merge trueCV {}", rc);

                            rc.trueCV = localC.trueCV;
                        }

                        // also copy cv
                        if (!Strings.isEmpty(localC.cv)) {

                            logger.debug("Merge cv {}", rc);
                            rc.cv = localC.cv;
                        }
                    }
                })
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void process(final Message<Game> message, MessageQueueProxy<Message> msgQueue) {

        var remoteGame = message.entity;

        logger.debug("Process {}", remoteGame);

        var localGame = GameQuery.fullTlp.query()
                .where(eq(remoteGame.id))
                .one();
        if (!Objects.equals(localGame, remoteGame)) {
            logger.debug("\nOld:{}\nNew:{}\n", localGame, remoteGame);
            gameDB.updateAll(remoteGame);
        }

        remoteGame.gameCharacters = merge(localGame.gameCharacters, remoteGame.gameCharacters);

        gameDB.updateChar(remoteGame);

        var localImgSize = Optional.ofNullable(localGame.gameImgs).map(List::size).orElse(0);
        var remoteImgSize = Optional.ofNullable(remoteGame.gameImgs).map(List::size).orElse(0);

        if (remoteImgSize > localImgSize) {

            logger.info("Game:{}", remoteGame.id);
            logger.info("Update GameImg:Local size:{},Remote size:{}", localImgSize, remoteImgSize);

            gameDB.updateImg(remoteGame);
        }


    }

}
