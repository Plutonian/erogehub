package com.goexp.galgame.gui.db.mongo.query;

import com.goexp.common.db.mongo.DBQueryTemplate;
import com.goexp.common.db.mongo.ObjectCreator;
import com.goexp.common.util.DateUtil;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.db.IGameQuery;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;

public class GameQuery implements IGameQuery {

    private final Logger logger = LoggerFactory.getLogger(GameQuery.class);

    private DBQueryTemplate tlp = new DBQueryTemplate("galgame", "game", new Creator.SimpleGame());


    @Override
    public List<Game> list(GameState gameState) {
        return tlp.list(
                eq("state", gameState.getValue())
                , exclude("gamechar", "simpleImg")
        );
    }

    @Override
    public List<Game> listByStarRange(int begin, int end) {
        return tlp.list(
                and(gte("star", begin), lte("star", end))
                , exclude("gamechar", "simpleImg")
                , descending("publishDate")
        );
    }

    @Override
    public List<Game> listByBrand(int brandId) {

        return tlp.list(
                eq("brandId", brandId)
                , exclude("gamechar", "simpleImg")
                , descending("publishDate")
        );

    }

    @Override
    public List<Game> list(int brandId, GameState gameState) {
        return tlp.list(
                and(eq("brandId", brandId), eq("state", gameState.getValue()))
                , exclude("gamechar", "simpleImg")
        );
    }

    @Override
    public List<Game> list(LocalDate start, LocalDate end) {
        return tlp.list(and(
                gte("publishDate", DateUtil.toDate(start.toString()+ " 00:00:00")),
                lte("publishDate", DateUtil.toDate(end.toString()+ " 23:59:59"))
                )
                , exclude("gamechar", "simpleImg")
                , descending("publishDate")
        );
    }

    @Override
    public List<Game> searchByName(String keyword) {

        return tlp.list(
                regex("name", "^" + keyword)
                , exclude("gamechar", "simpleImg")
        );
    }

    @Override
    public List<Game> searchByNameEx(String keyword) {

        return tlp.list(
                regex("name", keyword)
                , exclude("gamechar", "simpleImg")
        );
    }

    @Override
    public List<Game> searchByTag(String tag) {

        return tlp.list(
                or(eq("tag", tag), regex("name", tag))
                , exclude("gamechar", "simpleImg")
        );


    }

    @Override
    public List<Game> searchByPainter(String keyword) {

        return tlp.list(
                eq("painter", keyword)
                , exclude("gamechar", "simpleImg")
        );


    }

    @Override
    public List<Game> queryByCV(String keyword) {
        return tlp.list(
                eq("gamechar.truecv", keyword)
                , exclude("gamechar", "simpleImg")
        );


    }

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
                gameCharacter.gameId = gameid;

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
                g.brand = new Brand();
                g.brand.id = doc.getInteger("brandId");
                g.publishDate = Optional
                        .ofNullable(doc.getDate("publishDate"))
                        .map(date -> DateUtil.toLocalDate(date))
                        .orElse(null);

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

            @Override
            public Game create(Document doc) {
                var g = super.create(doc);
                var gamecharCreator = new GameChar(g.id);

                g.gameCharacters = ((List<Document>) doc.get("gamechar")).stream()
                        .map(gamecharCreator::create)
                        .collect(Collectors.toList());

                var simpleImgCreator = new SimpleImg();

                g.gameImgs = ((List<Document>) doc.get("simpleImg")).stream()
                        .map(simpleImgCreator::create)
                        .collect(Collectors.toList());

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
                        .collect(Collectors.toList());

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
                        .collect(Collectors.toList());

                return g;
            }
        }
    }

    public static class GameCharQuery {
        private DBQueryTemplate<Game> tlp = new DBQueryTemplate<Game>("galgame", "game", new Creator.CharList());

        public List<Game.GameCharacter> list(int gameId) {


            var g = tlp.one(
                    eq("_id", gameId)
                    , include("gamechar")
            );

            return g.gameCharacters;
        }
    }

    public static class GameImgQuery {

        private DBQueryTemplate<Game> tlp = new DBQueryTemplate<Game>("galgame", "game", new Creator.SimpleImgList());

        public List<Game.GameImg> list(int gameId) {

            var g = tlp.one(
                    eq("_id", gameId)
                    , include("simpleImg")
            );

            return g.gameImgs;
        }

    }


    private static class Test {

        public static void main(String[] args) {
            var q = new GameQuery();

            q.list(GameState.PLAYING).forEach(System.out::println);
        }
    }
}
