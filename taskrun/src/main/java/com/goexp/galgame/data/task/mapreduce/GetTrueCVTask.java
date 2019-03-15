package com.goexp.galgame.data.task.mapreduce;

import com.goexp.common.util.Strings;
import com.goexp.galgame.common.model.CV;
import com.goexp.galgame.data.db.importor.mongdb.GameDB;
import com.goexp.galgame.data.db.query.mongdb.CVQuery;
import com.goexp.galgame.data.db.query.mongdb.GameQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class GetTrueCVTask {

    private static GameDB gameDB = new GameDB();


    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(GetTrueCVTask.class);


        var cvMap = CV.getMap(CVQuery.tlp.query().list());


        logger.info("Init OK");

        GameQuery.fullTlp.query()
                .list()
                .parallelStream()
                .peek(game -> {

                    if (game.gameCharacters != null)
                        game.gameCharacters = game.gameCharacters.stream()
                                .peek(gameCharacter -> {
                                    if (!Strings.isEmpty(gameCharacter.cv)) {
                                        var cv = cvMap.get(gameCharacter.cv.trim().toLowerCase());

                                        if (cv != null) {
                                            gameCharacter.trueCV = cv.name;
//
                                            logger.debug("CV:{},trueCV:{}", gameCharacter.cv, gameCharacter.trueCV);
                                        }
                                    }
                                })
                                .collect(Collectors.toUnmodifiableList());
                })
                .forEach(game -> {
                    gameDB.updateChar(game);
                });
    }
}
