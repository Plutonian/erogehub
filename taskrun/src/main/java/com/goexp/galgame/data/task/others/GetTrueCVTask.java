package com.goexp.galgame.data.task.others;

import com.goexp.galgame.common.model.CV;
import com.goexp.galgame.data.db.importor.mysql.GameCharDB;
import com.goexp.galgame.data.db.query.GameCharService;
import com.goexp.galgame.data.db.query.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetTrueCVTask {
    private static GameCharDB importor = new GameCharDB();

    private static GameService cvQuery = new GameService();


    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(GameCharService.class);


        var cvMap = CV.getMap(cvQuery.cvList());


//        cvMap.entrySet().forEach(System.out::println);
//        System.out.println(cvMap);

        logger.info("Init OK");

        new GameCharService().listByCV()
                .parallelStream()
                .forEach(gameCharacter -> {
                    var cv = cvMap.get(gameCharacter.cv.trim().toLowerCase());

                    if (cv != null) {
                        gameCharacter.trueCV = cv.name;

                        logger.info("ID:{},CV:{},trueCV:{}", gameCharacter.id, gameCharacter.cv, gameCharacter.trueCV);

                        importor.updateTrueCV(gameCharacter);
                    }
                });
    }
}
