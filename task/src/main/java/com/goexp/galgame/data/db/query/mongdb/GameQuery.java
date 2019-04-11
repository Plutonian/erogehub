package com.goexp.galgame.data.db.query.mongdb;

import com.goexp.common.db.mongo.DBQueryTemplate;
import com.goexp.common.db.mongo.ObjectCreator;
import com.goexp.common.util.DateUtil;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.data.model.Game;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Projections.exclude;

public class GameQuery {

    public static final DBQueryTemplate<Game> fullTlp = new DBQueryTemplate.Builder<>("galgame",
            "game",
            new Creator.FullGame())
//            .defaultSelect(exclude("gamechar", "simpleImg"))
            .build();

    public static final DBQueryTemplate<Game> fullTlpWithChar = new DBQueryTemplate.Builder<>("galgame",
            "game",
            new Creator.FullGame())
            .defaultSelect(exclude("simpleImg"))
            .build();

    static class Creator {

        static class GameChar implements ObjectCreator<Game.GameCharacter> {
            private final Logger logger = LoggerFactory.getLogger(GameChar.class);

            @Override
            public Game.GameCharacter create(Document doc) {
                var gameCharacter = new Game.GameCharacter();
                gameCharacter.name = doc.getString("name");
                gameCharacter.cv = doc.getString("cv");
                gameCharacter.intro = doc.getString("intro");
                gameCharacter.trueCV = doc.getString("truecv");
                gameCharacter.img = doc.getString("img");
                gameCharacter.index = doc.getInteger("index");

                logger.debug("{}", gameCharacter);

                return gameCharacter;
            }
        }

        static class SimpleImg implements ObjectCreator<Game.GameImg> {

            private final Logger logger = LoggerFactory.getLogger(SimpleImg.class);

            @Override
            public Game.GameImg create(Document doc) {
                var gameImg = new Game.GameImg();
                gameImg.src = doc.getString("src");
                gameImg.index = doc.getInteger("index");
                logger.debug("{}", gameImg);

                return gameImg;
            }
        }

        static class SimpleGame implements ObjectCreator<Game> {

            private final Logger logger = LoggerFactory.getLogger(SimpleGame.class);

            @Override
            public Game create(Document doc) {
                var g = new Game();

                g.id = doc.getInteger("_id");
                g.name = doc.getString("name");
                g.brandId = Optional.ofNullable(doc.getInteger("brandId")).orElse(0);
                Optional.ofNullable(doc.getDate("publishDate"))
                        .ifPresent(date -> g.publishDate = DateUtil.toLocalDate(date));

                g.intro = doc.getString("intro");
                g.story = doc.getString("story");
                g.smallImg = doc.getString("smallImg");
                g.painter = (List<String>) doc.get("painter");
                g.writer = (List<String>) doc.get("writer");
                g.tag = (List<String>) doc.get("tag");
                g.type = (List<String>) doc.get("type");
                Optional.ofNullable(doc.getInteger("state"))
                        .map(GameState::from)
                        .ifPresent(gameState -> g.state = gameState);

                logger.debug("{}", g);


                return g;
            }
        }

        static class FullGame extends SimpleGame {

            GameChar gamecharCreator = new GameChar();
            SimpleImg simpleImgCreator = new SimpleImg();

            @Override
            public Game create(Document doc) {
                var g = super.create(doc);

                g.gameCharacters = Optional.ofNullable(doc.get("gamechar")).map(list -> {
                    return ((List<Document>) list).stream()
                            .map(gamecharCreator::create)
                            .collect(Collectors.toUnmodifiableList());
                }).orElse(List.of());
                g.gameImgs = Optional.ofNullable(doc.get("simpleImg")).map(list -> {
                    return ((List<Document>) list).stream()
                            .map(simpleImgCreator::create)
                            .collect(Collectors.toUnmodifiableList());
                }).orElse(List.of());

                return g;
            }
        }

    }
}
