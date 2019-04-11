package com.goexp.galgame.data.task.mapreduce;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.data.db.importor.mongdb.GameDB;
import com.goexp.galgame.data.db.query.mongdb.BrandQuery;
import com.goexp.galgame.data.db.query.mongdb.GameQuery;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.core.Piplline;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;
import com.goexp.galgame.data.task.handler.MesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static java.util.stream.Collectors.groupingBy;

public class MarkSameGameTask {

    private static final int UPDATE_STATE = 8;

    public static void main(String[] args) {

        new Piplline(new FromAllBrand())
                .registryCPUTypeMessageHandler(MesType.Brand, new ProcessBrandGame())
                .registryIOTypeMessageHandler(UPDATE_STATE, new UpdateState())
                .start();

    }

    public static class FromAllBrand extends DefaultStarter<Integer> {

        @Override
        public void process(MessageQueueProxy<Message> msgQueue) {

//            msgQueue.offer(new Message<>(MesType.Brand, 10143));

            BrandQuery.tlp.query().list()
                    .forEach(brand -> {
                        msgQueue.offer(new Message<>(MesType.Brand, brand.id));
                    });

        }

    }

    public static class ProcessBrandGame extends DefaultMessageHandler<Integer> {

        final private Logger logger = LoggerFactory.getLogger(ProcessBrandGame.class);

        private ProcessBrandGame() {
            try {
                try (var sameFileReader = new BufferedReader(new InputStreamReader(ProcessBrandGame.class.getResourceAsStream("/same.list")))) {
                    samelist = sameFileReader.lines().collect(Collectors.toUnmodifiableSet());
                }

                try (var packFileReader = new BufferedReader(new InputStreamReader(ProcessBrandGame.class.getResourceAsStream("/package.list")))) {
                    packagelist = packFileReader.lines().collect(Collectors.toUnmodifiableSet());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private Set<String> samelist;

        private Set<String> packagelist;


        @Override
        public void process(final Message<Integer> message, MessageQueueProxy<Message> msgQueue) {

            var brandId = message.entity;
            logger.debug("<Brand> {}", brandId);

            GameQuery.fullTlp.query()
                    .where(eq("brandId", brandId))
                    .list().stream()

                    //partitioning
                    .collect(groupingBy(game -> {
                        if (checkSameGamePredicate().test(game)) {
                            return "same";
                        } else if (checkpackageGamePredicate().test(game)) {
                            return "package";
                        } else {
                            return "other";
                        }
                    }))
                    .entrySet().stream()
                    .flatMap(groupEntry -> {

                        final var value = groupEntry.getValue();
                        final var key = groupEntry.getKey();
                        switch (key) {
                            case "same": {
                                return value.stream()
                                        .filter(game -> game.state == GameState.UNCHECKED)
                                        .peek(game -> game.state = GameState.SAME);
                            }
                            case "package": {
                                return value.stream()
                                        .filter(game -> game.state == GameState.UNCHECKED)
                                        .peek(game -> game.state = GameState.PACKAGE);
                            }
                            case "other": {
                                return value.stream()
                                        .filter(game -> game.publishDate != null)
                                        .collect(groupingBy(game -> game.publishDate))

                                        .entrySet().stream()
                                        .filter(localDateListEntry -> localDateListEntry.getValue().size() > 1)
                                        .flatMap(localDateListEntry -> {

                                            //games by date
                                            return localDateListEntry.getValue().stream()

                                                    // only min-name-length is the target
                                                    .sorted(Comparator.comparing(game -> game.name.length()))
                                                    .skip(1)
                                                    //only unchecked
                                                    .filter(game -> game.state == GameState.UNCHECKED)
                                                    .peek(game -> game.state = GameState.SAME);

                                        });
                            }
                            default: {
                                throw new RuntimeException("Error");
                            }
                        }

                    })
                    .forEach(game -> {

                        logger.info("ID:{} Name: {}  State: {}", game.id, game.name, game.state);
                        msgQueue.offer(new Message<>(UPDATE_STATE, game));
                    });

        }

        private Predicate<Game> checkSameGamePredicate() {
            return game -> samelist.stream().anyMatch(str -> game.name.contains(str));
        }

        private Predicate<Game> checkpackageGamePredicate() {
            return game -> Optional.ofNullable(game.type).orElse(List.of()).contains("セット商品") ||
                    packagelist.stream().anyMatch(str -> game.name.contains(str));
        }

    }

    public static class UpdateState extends DefaultMessageHandler<Game> {

        final private Logger logger = LoggerFactory.getLogger(UpdateState.class);

        final private GameDB.StateDB stateDB = new GameDB.StateDB();

        @Override
        public void process(final Message<Game> message, MessageQueueProxy<Message> msgQueue) {

            var game = message.entity;
            logger.debug("<Game> {}", game.id);

            stateDB.update(game);
        }

    }


}
