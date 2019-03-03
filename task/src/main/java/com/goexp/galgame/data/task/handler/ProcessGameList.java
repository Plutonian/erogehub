package com.goexp.galgame.data.task.handler;

import com.goexp.galgame.data.db.importor.mongdb.GameDB;
import com.goexp.galgame.data.db.query.mongdb.GameQuery;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.task.download.contentprovider.ListProvider;
import com.goexp.galgame.data.task.download.contentprovider.brand.LocalProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ProcessGameList extends DefaultMessageHandler<Integer> {

    final private Logger logger = LoggerFactory.getLogger(ProcessGameList.class);

    final private GameQuery gameService = new GameQuery();
    final private GameDB importor = new GameDB();
    final private ListProvider listProvider = new LocalProvider();

    @Override
    public void process(final Message<Integer> message, BlockingQueue<Message> msgQueue) {

        var brandId = message.entity;
        logger.debug("<Brand> {}", brandId);
        try {

            var parseGameList = listProvider.getList(brandId);

            final var indbList = gameService.listByBrand(brandId);

            Optional.ofNullable(parseGameList).ifPresent((list) -> {


                if (list.size() != indbList.size()) {
                    logger.debug("Brand:{},RemoteCount:{},LocalCount:{}", brandId, list.size(), indbList.size());
                    list.stream()
                            .filter(g -> !indbList.contains(g.id))
                            .forEach(newGame -> {
                                newGame.brandId = brandId;

                                logger.info("<Insert> {}", newGame.simpleView());
                                importor.insert(newGame);

                                try {
                                    msgQueue.offer(new Message<>(MesType.NEED_DOWN_GAME, newGame.id), 60, TimeUnit.SECONDS);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });

                }

            });
        } catch (Exception e) {
            logger.error("Brand:{}", brandId);

            e.printStackTrace();
        }

//        var ids = list.idsByBrand(bid);
//
//        for (var id : ids) {
//
//            try {
//                msgQueue.offer(new Message<>(MesType.Game, id), 60, TimeUnit.SECONDS);
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

}
