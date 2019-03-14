package com.goexp.galgame.data.task.mapreduce;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.data.db.importor.mongdb.GameDB;
import com.goexp.galgame.data.db.query.mongdb.BrandQuery;
import com.goexp.galgame.data.db.query.mongdb.GameQuery;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.Piplline;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;
import com.goexp.galgame.data.task.handler.MesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MarkSameGameTask {

    private static final int UPDATE_STATE = 8;

    public static class FromAllBrand extends DefaultStarter<Integer> {

        final private BrandQuery brandQuery = new BrandQuery();

        @Override
        public void process(BlockingQueue<Message> msgQueue) {


            brandQuery.list().forEach(brand -> {

                try {
                    msgQueue.offer(new Message(MesType.Brand, brand.id), 60, TimeUnit.SECONDS);
                } catch (Exception e) {
//                        e.printStackTrace();
                }

            });

        }

    }


    public static class ProcessGameList extends DefaultMessageHandler<Integer> {

        final private Logger logger = LoggerFactory.getLogger(com.goexp.galgame.data.task.handler.ProcessGameList.class);

        final private GameQuery gameService = new GameQuery();

        private static final Pattern NAME_SPLITER_REX = Pattern.compile("[〜＜＋（「\\s]");

        final static Set<String> checklist = Set.of(
                "げっちゅ屋Ver",
                "廉価版",
                "タペストリ",
                "普及版",
                "Best Windows",
                "KAGUYAコレクション",
                "ベストシリーズ",
                "破格版",
                "セレクション",
                "シンプル版"
        );

        @Override
        public void process(final Message<Integer> message, BlockingQueue<Message> msgQueue) {

            var brandId = message.entity;
            logger.debug("<Brand> {}", brandId);

            var parseGameList = gameService.listByBrand(brandId);


            Optional.ofNullable(parseGameList).ifPresent((list) -> {

                list.stream()
                        .collect(Collectors.groupingBy(game -> {
                            var matcher = NAME_SPLITER_REX.matcher(game.name);
                            return matcher.find() ? game.name.substring(0, matcher.start()) : game.name;
                        }))
                        .entrySet().stream()
                        .flatMap(stringListEntry -> {

                            if (stringListEntry.getValue().size() == 1) {
                                return stringListEntry.getValue().stream()
                                        .filter(game -> checklist.stream().anyMatch(str -> game.name.contains(str)))
                                        .peek(game -> game.state = GameState.SAME);
                            } else {
                                //has some

                                return stringListEntry.getValue().stream()
                                        //split
                                        .collect(Collectors.partitioningBy(game -> {
                                            return checklist.stream().anyMatch(str -> game.name.contains(str));
                                        }))
                                        .entrySet().stream()
                                        .flatMap(group -> {

                                            //SMAE
                                            if (group.getKey()) {
                                                return group.getValue().stream()
                                                        .peek(game -> game.state = GameState.SAME);
                                            } else {
                                                var templist = group.getValue().stream()
                                                        .sorted(Comparator.comparing(game -> game.name.length()))
                                                        .filter(game -> game.state == GameState.UNCHECKED)
                                                        .peek(game -> game.state = GameState.SAME)
                                                        .collect(Collectors.toUnmodifiableList());

                                                if (templist.size() > 0)
                                                    templist.get(0).state = GameState.UNCHECKED;

                                                return templist.stream();
                                            }
                                        });

                            }

                        })
                        .forEach(game -> {

                            try {
                                msgQueue.offer(new Message<>(UPDATE_STATE, game), 60, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });

            });

        }

    }

    public static class FillProcessGameList extends DefaultMessageHandler<Integer> {

        final private Logger logger = LoggerFactory.getLogger(com.goexp.galgame.data.task.handler.ProcessGameList.class);

        final private GameQuery gameService = new GameQuery();


        @Override
        public void process(final Message<Integer> message, BlockingQueue<Message> msgQueue) {

            var brandId = message.entity;
            logger.debug("<Brand> {}", brandId);

            var parseGameList = gameService.listByBrand(brandId);


            Optional.ofNullable(parseGameList).ifPresent((list) -> {

                list.stream()
                        .forEach(game -> {
                            game.state = GameState.UNCHECKED;
                            try {
                                msgQueue.offer(new Message<>(UPDATE_STATE, game), 60, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });

            });

        }

    }

    public static class UpdateState extends DefaultMessageHandler<Game> {

        final private Logger logger = LoggerFactory.getLogger(UpdateState.class);

        final private GameDB.StateDB stateDB = new GameDB.StateDB();

        @Override
        public void process(final Message<Game> message, BlockingQueue<Message> msgQueue) {

            var game = message.entity;
            logger.debug("<Game> {}", game.id);

            stateDB.update(game);
        }

    }

    public static void main(String[] args) {

        var pipl = new Piplline(new FromAllBrand());

        pipl.registryCPUTypeMessageHandler(MesType.Brand, new ProcessGameList());
        pipl.registryIOTypeMessageHandler(UPDATE_STATE, new UpdateState());

        pipl.start();

    }


}
