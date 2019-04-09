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

    public static final DBQueryTemplate<Game> fullTlp = new DBQueryTemplate.Builder<Game>("galgame",
            "game",
            new Creator.FullGame())
//            .defaultSelect(exclude("gamechar", "simpleImg"))
            .build();

    public static final DBQueryTemplate<Game> fullTlpWithChar = new DBQueryTemplate.Builder<Game>("galgame",
            "game",
            new Creator.FullGame())
            .defaultSelect(exclude("simpleImg"))
            .build();

    static class Creator {

        static class GameChar implements ObjectCreator<Game.GameCharacter> {
            private final Logger logger = LoggerFactory.getLogger(GameChar.class);

            private final int gameid;

            public GameChar(int gameid) {
                this.gameid = gameid;
            }

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
                g.brandId = doc.getInteger("brandId");
                g.publishDate = Optional.ofNullable(doc.getDate("publishDate"))
                        .map(DateUtil::toLocalDate)
                        .orElseGet(null);

                g.intro = doc.getString("intro");
                g.story = doc.getString("story");
                g.smallImg = doc.getString("smallImg");
                g.painter = (List<String>) doc.get("painter");
                g.writer = (List<String>) doc.get("writer");
                g.tag = (List<String>) doc.get("tag");
                g.type = (List<String>) doc.get("type");
                g.state = Optional.ofNullable(doc.getInteger("state"))
                        .map(GameState::from).orElseGet(null);

                logger.debug("{}", g);


                return g;
            }
        }

        static class FullGame extends SimpleGame {

            @Override
            public Game create(Document doc) {
                var g = super.create(doc);


                Optional.ofNullable(doc.get("gamechar"))
                        .ifPresent(list -> {

                            var gamecharCreator = new GameChar(g.id);
                            g.gameCharacters = ((List<Document>) list).stream()
                                    .map(gamecharCreator::create)
                                    .collect(Collectors.toUnmodifiableList());
                        });


                Optional.ofNullable(doc.get("simpleImg"))
                        .ifPresent(list -> {
                            var simpleImgCreator = new SimpleImg();

                            g.gameImgs = ((List<Document>) list).stream()
                                    .map(simpleImgCreator::create)
                                    .collect(Collectors.toUnmodifiableList());

                        });

                return g;
            }
        }

        static class CharList extends SimpleGame {

            @Override
            public Game create(Document doc) {
                var g = new Game();
                var gamecharCreator = new GameChar(g.id);

                g.gameCharacters = ((List<Document>) doc.get("gamechar")).stream()
                        .map(gamecharCreator::create)
                        .collect(Collectors.toUnmodifiableList());

                return g;
            }
        }

        static class SimpleImgList extends SimpleGame {

            @Override
            public Game create(Document doc) {
                var g = new Game();

                var simpleImgCreator = new SimpleImg();

                g.gameImgs = ((List<Document>) doc.get("simpleImg")).stream()
                        .map(simpleImgCreator::create)
                        .collect(Collectors.toUnmodifiableList());

                return g;
            }
        }
    }
}
