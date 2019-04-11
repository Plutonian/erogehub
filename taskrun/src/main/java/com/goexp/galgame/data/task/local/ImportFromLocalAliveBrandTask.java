package com.goexp.galgame.data.task.local;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.common.util.Network;
import com.goexp.galgame.data.db.importor.mongdb.GameDB;
import com.goexp.galgame.data.db.query.mongdb.BrandQuery;
import com.goexp.galgame.data.db.query.mongdb.GameQuery;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.core.Piplline;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;
import com.goexp.galgame.data.task.download.contentprovider.brand.LocalProvider;
import com.goexp.galgame.data.task.handler.MesType;
import com.goexp.galgame.data.task.handler.game.Bytes2Html;
import com.goexp.galgame.data.task.handler.game.Html2GameOK;
import com.goexp.galgame.data.task.handler.game.LocalGameHandler;
import com.goexp.galgame.data.task.handler.game.ProcessGameOK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.eq;

public class ImportFromLocalAliveBrandTask {

    public static void main(String[] args) {

        Network.initProxy();

        new Piplline(new StartFromAllAliveBrand())
                .registryCPUTypeMessageHandler(MesType.Brand, new ProcessGameList())
                .registryCPUTypeMessageHandler(MesType.Game, new LocalGameHandler())
                .registryCPUTypeMessageHandler(MesType.ContentBytes, new Bytes2Html())
                .registryCPUTypeMessageHandler(MesType.ContentHtml, new Html2GameOK())
                .registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameOK())
                .start();

    }

    public static class StartFromAllAliveBrand extends DefaultStarter<Integer> {


        @Override
        public void process(MessageQueueProxy<Message> msgQueue) {
            BrandQuery.tlp.query()
                    .list()
                    .forEach(brand -> {
                        msgQueue.offer(new Message(MesType.Brand, brand.id));
                    });


            System.out.println("All Done!!!");
        }

    }

    public static class ProcessGameList extends DefaultMessageHandler<Integer> {

        final private Logger logger = LoggerFactory.getLogger(ProcessGameList.class);

        final private GameDB importor = new GameDB();

        @Override
        public void process(final Message<Integer> message, MessageQueueProxy<Message> msgQueue) {

            var brandId = message.entity;
            logger.debug("<Brand> {}", brandId);
            try {

                var parseGameList = LocalProvider.getList(brandId);

                final var indbList = GameQuery.fullTlp.query()
                        .where(eq("brandId", brandId))
                        .list();


                if (parseGameList.size() > indbList.size()) {

                    logger.debug("Brand:{},RemoteCount:{},LocalCount:{}", brandId, parseGameList.size(), indbList.size());

                    parseGameList.removeAll(indbList);

                    parseGameList.stream().forEach(newGame -> {
                        newGame.brandId = brandId;
                        newGame.state = GameState.UNCHECKED;

                        logger.info("<Insert> {}", newGame.simpleView());
                        importor.insert(newGame);

                        msgQueue.offer(new Message<>(MesType.Game, newGame.id));
                    });

                }

            } catch (Exception e) {
                logger.error("Brand:{}", brandId);

                e.printStackTrace();
            }

        }

    }


}
