package com.goexp.galgame.data.task.others;

import com.goexp.galgame.common.model.CV;
import com.goexp.galgame.data.db.query.mongdb.CVQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetTrueCVTask {

    private static CVQuery cvQuery = new CVQuery();


    public static void main(String[] args) {

        //:TODO GET_TRUE_CV

        final Logger logger = LoggerFactory.getLogger(GetTrueCVTask.class);


        var cvMap = CV.getMap(cvQuery.cvList());


        logger.info("Init OK");

//        new GameCharService().listByCV()
//                .parallelStream()
//                .forEach(gameCharacter -> {
//                    var cv = cvMap.get(gameCharacter.cv.trim().toLowerCase());
//
//                    if (cv != null) {
//                        gameCharacter.trueCV = cv.name;
//
//                        logger.info("CV:{},trueCV:{}", gameCharacter.cv, gameCharacter.trueCV);

//                        importor.updateTrueCV(gameCharacter);
//                    }
//                });
    }
}
