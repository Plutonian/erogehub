package com.goexp.galgame.data.task.others;

import com.goexp.galgame.common.util.Network;
import com.goexp.galgame.data.Config;
import com.goexp.galgame.data.db.importor.mongdb.GameDB;
import com.goexp.galgame.data.db.query.mongdb.GameQuery;
import com.goexp.galgame.data.task.handler.MesType;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.Piplline;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;
import com.goexp.galgame.data.task.download.contentprovider.ListProvider;
import com.goexp.galgame.data.task.download.contentprovider.brand.LocalProvider;
import com.goexp.galgame.data.task.download.provider.IdsProvider;
import com.goexp.galgame.data.task.download.provider.brand.DBIdsProvider;
import com.goexp.galgame.data.task.download.provider.brand.ErrorIDSProvider;
import com.goexp.galgame.data.task.handler.game.Bytes2Html;
import com.goexp.galgame.data.task.handler.game.Html2GameOK;
import com.goexp.galgame.data.task.handler.game.LocalGameHandler;
import com.goexp.galgame.data.task.handler.game.ProcessGameOK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ImportFromLocalAliveBrandTask {

    public static class StartFromAllAliveBrand extends DefaultStarter<Integer> {


        @Override
        public void process(BlockingQueue<Message> msgQueue) {
            IdsProvider idsProvider = new DBIdsProvider();
            var ids = idsProvider.getIds();
            down(ids, msgQueue);

            while (Files.exists(Config.BRAND_ERROR_FILE)) {
                down(new ErrorIDSProvider().getIds(), msgQueue);
            }

            System.out.println("All Done!!!");
        }

        private void down(List<Integer> ids, BlockingQueue<Message> msgQueue) {
            ids.parallelStream()
                    .forEach(id -> {

                        try {
                            msgQueue.offer(new Message(MesType.Brand, id), 60, TimeUnit.SECONDS);
                        } catch (Exception e) {
                        }

                    });
        }
    }

    public static class BrandIds extends DefaultStarter<Integer> {


        @Override
        public void process(BlockingQueue<Message> msgQueue) {
            try {
                msgQueue.offer(new Message(MesType.Brand, 3), 60, TimeUnit.SECONDS);
            } catch (Exception e) {
            }

            System.out.println("All Done!!!");
        }

    }

    public static class ProcessGameList extends DefaultMessageHandler<Integer> {

        final private Logger logger = LoggerFactory.getLogger(com.goexp.galgame.data.task.handler.ProcessGameList.class);

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


                    if (list.size() > indbList.size()) {

                        logger.debug("Brand:{},RemoteCount:{},LocalCount:{}", brandId, list.size(), indbList.size());

                        parseGameList.removeAll(indbList);

                        parseGameList.stream()
                                .forEach(newGame -> {
                                    newGame.brandId = brandId;

                                    logger.info("<Insert> {}", newGame.simpleView());
                                    importor.insert(newGame);

                                    try {
                                        msgQueue.offer(new Message<>(MesType.Game, newGame.id), 60, TimeUnit.SECONDS);
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

    public static void main(String[] args) throws IOException {

        Network.initProxy();


        var pipl = new Piplline(new StartFromAllAliveBrand());

        pipl.registryCPUTypeMessageHandler(MesType.Brand, new ProcessGameList());
        pipl.registryCPUTypeMessageHandler(MesType.Game, new LocalGameHandler());
        pipl.registryCPUTypeMessageHandler(MesType.ContentBytes, new Bytes2Html());
        pipl.registryCPUTypeMessageHandler(MesType.ContentHtml, new Html2GameOK());
        pipl.registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameOK());
//        pipl.registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameCharOK());
//        pipl.registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameImgOK());

        pipl.start();

    }


}
