package com.goexp.galgame.data.task.handler.game;

import com.goexp.galgame.data.db.importor.mysql.GameImgDB;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class ProcessGameImgOK extends DefaultMessageHandler<Game> {

    final private GameImgDB imgDBImportor = new GameImgDB();

    final private Logger logger = LoggerFactory.getLogger(ProcessGameImgOK.class);

    @Override
    public void process(final Message<Game> message, BlockingQueue<Message> msgQueue) {

        var game = message.entity;

        game.imgList.forEach(gameImg -> {
            if (!imgDBImportor.exist(gameImg.id)) {

                logger.info("Insert:{}", gameImg);
                imgDBImportor.insert(gameImg);
            }

        });

    }


}
