package com.goexp.galgame.data.task.others;

import com.goexp.common.util.ContentLoader;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.parser.GameGuideParser;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.Piplline;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class GameGuideLocalImportTask {

    private static final Charset CHARSET = Charset.forName("shift-jis");

    private static class Starter extends DefaultStarter<Map<String, Object>> {

        @Override
        public void process(BlockingQueue<Message> msgQueue) {
            var path = Path.of("E:/info/c/sagaoz.net/foolmaker/game.html");

            if (!Files.exists(path)) {
                System.out.println("Not exist");
                return;
            }

            var html = ContentLoader.loadFromFile(path, CHARSET);


            new GameGuideParser.Sagaoz_Net().parse(html, a -> {
                var guide = new Game.Guide();
                guide.title = a.text();
                guide.id = a.attr("href").replace("/", "_");
                guide.from = Game.Guide.DataFrom.sagaoz_net;

                var param = Map.of("guide", guide, "path", a.attr("href"));


                try {
                    msgQueue.offer(new Message<>(1, param), 10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });


        }
    }

    private static class PageContentHandler extends DefaultMessageHandler<Map<String, Object>> {

        private static final Path root = Path.of("E:/info/c/sagaoz.net/foolmaker/");

        @Override
        public void process(Message<Map<String, Object>> message, BlockingQueue<Message> msgQueue) {
            var map = message.entity;

            var path = root.resolve((String) map.get("path"));

            var html = ContentLoader.loadFromFile(path, CHARSET);
            var guide = (Game.Guide) map.get("guide");
            guide.content = html;
            System.out.println(guide);
        }
    }

    public static void main(String[] args) {


        var pipl = new Piplline(new Starter());

        pipl.registryCPUTypeMessageHandler(1, new PageContentHandler());

        pipl.start();

    }
}
