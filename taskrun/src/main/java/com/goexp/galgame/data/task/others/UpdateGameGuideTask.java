package com.goexp.galgame.data.task.others;

import com.goexp.galgame.common.model.CommonGame;
import com.goexp.galgame.common.util.Network;
import com.goexp.galgame.data.db.importor.mongdb.GuideDB;
import com.goexp.galgame.data.db.query.mongdb.GuideQuery;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.parser.GameGuideParser;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.core.Piplline;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import static com.goexp.common.util.WebUtil.httpClient;
import static com.goexp.common.util.WebUtil.noneProxyClient;
import static com.mongodb.client.model.Filters.eq;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

public class UpdateGameGuideTask {
    private static final Charset CHARSET = Charset.forName("shift-jis");

    private static class PageContentHandler extends DefaultMessageHandler<Game.Guide> {

        private static final GuideDB guideDb = new GuideDB();
        final Logger logger = LoggerFactory.getLogger(UpdateGameGuideTask.class);

        @Override
        public void process(Message<Game.Guide> message, MessageQueueProxy<Message> msgQueue) {
            var guide = message.entity;
            logger.info("insert:{}", guide);
            guideDb.insert(guide);
        }
    }

    static class Sagaoz_Net {


        public static void main(String[] args) {

            var pipl = new Piplline(new Starter());

            pipl.registryIOTypeMessageHandler(1, new PageContentHandler());

            pipl.start();

        }

        private static class Starter extends DefaultStarter<Map<String, Object>> {
            final Logger logger = LoggerFactory.getLogger(DefaultStarter.class);

            @Override
            public void process(MessageQueueProxy<Message> msgQueue) {


                var locallist = GuideQuery.tlp.query()
                        .where(eq("from", CommonGame.Guide.DataFrom.sagaoz_net.getValue()))
                        .list();


                logger.info("Local:{}", locallist.size());

                var req = HttpRequest.newBuilder()
                        .uri(URI.create("http://sagaoz.net/foolmaker/game.html"))
                        .build();

                try {
                    var html = noneProxyClient().send(req, ofString(CHARSET)).body();

                    var remoteList = new GameGuideParser.Sagaoz_Net().parse(html);

                    logger.info("Remote:{}", remoteList.size());


                    var insertlist = new ArrayList<>(remoteList);
                    insertlist.removeAll(locallist);


                    logger.info("Insert:{}", insertlist.size());


                    insertlist.stream()
                            .forEach(guide -> {
                                msgQueue.offer(new Message<>(1, guide));
                            });

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    static class Seiya_saiga {

        public static void main(String[] args) {

            Network.initProxy();
            var pipl = new Piplline(new Starter());

            pipl.registryIOTypeMessageHandler(1, new PageContentHandler());

            pipl.start();

        }

        private static class Starter extends DefaultStarter<Game.Guide> {
            final Logger logger = LoggerFactory.getLogger(DefaultStarter.class);

            @Override
            public void process(MessageQueueProxy<Message> msgQueue) {


                var locallist = GuideQuery.tlp.query()
                        .where(eq("from", CommonGame.Guide.DataFrom.seiya_saiga_com.getValue()))
                        .list();


                logger.info("Local:{}", locallist.size());

                var req = HttpRequest.newBuilder()
                        .uri(URI.create("http://seiya-saiga.com/game/kouryaku.html"))
                        .build();

                try {
                    var html = httpClient.send(req, ofString(CHARSET)).body();

                    var remoteList = new GameGuideParser.Seiya_saiga().parse(html);


                    logger.info("Remote:{}", remoteList.size());


                    var insertlist = new ArrayList<>(remoteList);
                    insertlist.removeAll(locallist);


                    logger.info("Insert:{}", insertlist.size());

                    insertlist.stream()
                            .distinct()
                            .forEach(guide -> {
                                msgQueue.offer(new Message<>(1, guide));
                            });

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    }


}
