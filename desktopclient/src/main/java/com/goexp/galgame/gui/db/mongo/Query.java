package com.goexp.galgame.gui.db.mongo;

import com.goexp.common.db.mongo.DBQueryTemplate;
import com.goexp.common.db.mongo.ObjectCreator;
import com.goexp.common.util.DateUtil;
import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.common.model.CV;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.common.model.TagType;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;

public class Query {

    private static final String DB = "galgame";

    public static class BrandQuery {

        public final static DBQueryTemplate<Brand> tlp = new DBQueryTemplate.Builder<>(DB, "brand", new BrandCreator()).build();

        private static class BrandCreator implements ObjectCreator<Brand> {

            private final Logger logger = LoggerFactory.getLogger(BrandCreator.class);

            @Override
            public Brand create(Document doc) {
                var g = new Brand();

                logger.debug("<create> doc={}", doc);

                g.id = doc.getInteger("_id");
                g.name = doc.getString("name");
                g.website = doc.getString("website");
                g.isLike = BrandType.from(doc.getInteger("type"));
                g.comp = doc.getString("comp");
                return g;
            }
        }
    }

    public static class CVQuery {

        public final static DBQueryTemplate<CV> tlp = new DBQueryTemplate.Builder<>(DB, "cv", new CVCreator()).build();

        private static class CVCreator implements ObjectCreator<CV> {

            private final Logger logger = LoggerFactory.getLogger(CVCreator.class);

            @Override
            public CV create(Document doc) {
                var g = new CV();

                logger.debug("<create> doc={}", doc);

                g.name = doc.getString("name");
                g.star = doc.getInteger("star");
                g.nameStr = doc.getString("names");
                return g;
            }
        }
    }

    public static class GameQuery {

        public final static DBQueryTemplate<Game> tlp =
                new DBQueryTemplate.Builder<Game>(DB, "game", new Creator.FullGame())
                        .defaultSelect(exclude("gamechar", "simpleImg"))
                        .defaultSort(descending("publishDate", "name"))
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

            static abstract class SimpleGame implements ObjectCreator<Game> {

                private final Logger logger = LoggerFactory.getLogger(SimpleGame.class);

                @Override
                public Game create(Document doc) {
                    var g = new Game();

                    g.id = doc.getInteger("_id");
                    g.name = doc.getString("name");
                    g.brand = new Brand();
                    g.brand.id = doc.getInteger("brandId");
                    Optional.ofNullable(doc.getDate("publishDate"))
                            .ifPresent(date -> g.publishDate = DateUtil.toLocalDate(date));

                    g.intro = doc.getString("intro");
                    g.story = doc.getString("story");
                    g.smallImg = doc.getString("smallImg");
                    g.painter = (List<String>) doc.get("painter");
                    g.writer = (List<String>) doc.get("writer");
                    g.tag = (List<String>) doc.get("tag");
                    g.type = (List<String>) doc.get("type");
                    g.setState(GameState.from(doc.getInteger("state")));
                    g.star = doc.getInteger("star");

                    logger.debug("{}", g);


                    return g;
                }
            }

            static class FullGame extends SimpleGame {

                private final GameChar gamecharCreator = new GameChar();
                private final SimpleImg simpleImgCreator = new SimpleImg();

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

        public static class GameCharQuery {
            public static final DBQueryTemplate<Game> tlp = new DBQueryTemplate.Builder<>(DB, "game", new Creator.FullGame())
                    .defaultSelect(include("gamechar"))
                    .build();
        }

        public static class GameImgQuery {
            public static final DBQueryTemplate<Game> tlp = new DBQueryTemplate.Builder<>(DB, "game", new Creator.FullGame())
                    .defaultSelect(include("simpleImg"))
                    .build();
        }
    }

    public static class TagQuery {

        public final static DBQueryTemplate<TagType> tlp = new DBQueryTemplate.Builder<>(DB, "tag", new TagCreator()).build();

        private static class TagCreator implements ObjectCreator<TagType> {
            @Override
            public TagType create(Document doc) {
                var t = new TagType();
                t.type = doc.getString("type");
                t.order = doc.getInteger("order");
                t.tags = (List<String>) doc.get("tags", List.class);
                return t;
            }
        }

    }
}
