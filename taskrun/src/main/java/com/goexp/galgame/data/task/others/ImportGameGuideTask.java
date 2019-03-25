package com.goexp.galgame.data.task.others;

import com.goexp.common.util.WebUtil;
import com.goexp.galgame.common.util.Network;
import com.goexp.galgame.data.db.importor.mongdb.GuideDB;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.net.http.HttpResponse.BodyHandlers.ofString;

public class ImportGameGuideTask {
    private static final Charset CHARSET = Charset.forName("shift-jis");

    private static class PageContentHandler extends DefaultMessageHandler<Game.Guide> {

        private static final GuideDB guideDb = new GuideDB();
        final Logger logger = LoggerFactory.getLogger(ImportGameGuideTask.class);

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


            @Override
            public void process(MessageQueueProxy<Message> msgQueue) {

                var req = HttpRequest.newBuilder()
                        .uri(URI.create("http://sagaoz.net/foolmaker/game.html"))
                        .build();

                try {
                    var res = WebUtil.noneProxyClient().send(req, ofString(CHARSET));
                    var html = res.body();

                    var list = new GameGuideParser.Sagaoz_Net().parse(html);


                    list.stream()
                            .distinct()
                            .forEach(guide -> {

                                msgQueue.offer(new Message<>(1, guide), 10, TimeUnit.SECONDS);
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


            @Override
            public void process(MessageQueueProxy<Message> msgQueue) {

                var req = HttpRequest.newBuilder()
                        .uri(URI.create("http://seiya-saiga.com/game/kouryaku.html"))
                        .build();

                try {
                    var res = WebUtil.httpClient.send(req, ofString(CHARSET));
                    var html = res.body();

                    var list = new GameGuideParser.Seiya_saiga().parse(html);

                    list.stream()
                            .distinct()
                            .forEach(guide -> {
                                msgQueue.offer(new Message<>(1, guide), 10, TimeUnit.SECONDS);
                            });

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    }


}
