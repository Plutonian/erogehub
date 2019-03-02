package com.goexp.galgame.gui.db.mysql;

import com.goexp.common.db.mongo.DBOperatorTemplate;
import com.goexp.galgame.gui.db.mysql.query.GameQuery;
import com.goexp.galgame.gui.db.mysql.query.TagQuery;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;

import java.time.LocalDate;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.exclude;


public class Mysql2Mongo {

    public static void main(String[] args) {


        var gameQ = new GameQuery.Export();
        var gcharQ = new GameQuery.GameCharQuery();
        var gImgQ = new GameQuery.GameImgQuery();

//        var games = gameQ.list(
//                LocalDate.now().withMonth(1).withDayOfMonth(1),
//                LocalDate.now().withMonth(1).withDayOfMonth(31)
//                LocalDate.of(2000, 1, 1),
//                LocalDate.of(2018, 12, 31)
//        );


        var games = gameQ.list(
                LocalDate.now().withMonth(1).withDayOfMonth(1),
                LocalDate.now().withMonth(12).withDayOfMonth(31)
//                LocalDate.of(2018, 12, 31)
        );


        var docs = games.stream()
                .peek(game -> {
                    game.gameCharacters = gcharQ.list(game.id);
                    game.gameImgs = gImgQ.list(game.id);
                })
                .map(game -> {
//                    System.out.println(game);

                    var gameCharDocs = game.gameCharacters
                            .stream()
                            .map(gameCharacter -> {
                                var doc = new Document("name", gameCharacter.name)
                                        .append("intro", gameCharacter.intro)
                                        .append("cv", gameCharacter.cv)
                                        .append("truecv", gameCharacter.trueCV)
                                        .append("img", gameCharacter.img)
                                        .append("index", gameCharacter.index);


                                return doc;

                            })
                            .collect(Collectors.toList());

                    var imgdocs = game.gameImgs.stream()
                            .map(img -> {
                                var doc = new Document("src", img.src)
                                        .append("index", img.index);

                                return doc;
                            })
                            .collect(Collectors.toList());

                    var gameDoc = new Document("_id", game.id)
                            .append("name", game.name)
                            .append("publishDate", game.publishDate)
                            .append("smallImg", game.smallImg)
                            .append("writer", game.writer)
                            .append("painter", game.painter)
                            .append("type", game.type)
                            .append("tag", game.tag)
                            .append("story", game.story)
                            .append("intro", game.intro)
                            .append("state", game.state.get().getValue())
                            .append("star", game.star)
                            .append("brandId", game.brand.id)
                            .append("gamechar", gameCharDocs)
                            .append("simpleImg", imgdocs);

                    return gameDoc;

                })
                .collect(Collectors.toList());

        var tlp = new DBOperatorTemplate("galgame", "game");
        tlp.exec(gameC -> {
            gameC.insertMany(docs);

        });
    }

    static class Tags {
        public static void main(String[] args) {
            var tagQ = new TagQuery();
            var docs = tagQ.types().stream()
                    .collect(Collectors.groupingBy(tag -> tag.type))
                    .entrySet().stream()
                    .map((entry) -> {
                        var gameDoc = new Document("type", entry.getKey().type)
                                .append("order", entry.getKey().order);


                        var tags = entry.getValue().stream()
                                .map(tag -> tag.name)
                                .collect(Collectors.toList());

                        gameDoc.append("tags", tags);


                        return gameDoc;
                    })
                    .collect(Collectors.toList());


            MongoClient mongoClient = MongoClients.create();

            var db = mongoClient.getDatabase("galgame");
            var gameC = db.getCollection("tag");
            gameC.insertMany(docs);


            mongoClient.close();
        }
    }

    static class Show {

        public static void main(String[] args) {
            var tlp = new DBOperatorTemplate("galgame", "game");
            tlp.exec((c -> {

                var searchObject = new BasicDBObject();
                searchObject.put("founder", "name");

                for (var d : c.find(eq("_id", 1)).projection(exclude("gamechar", "simpleImg"))) {
                    System.out.println(d.toJson());
                }

            }));
        }
    }

    static class Delete {

        public static void main(String[] args) {
            MongoClient mongoClient = MongoClients.create();

            var db = mongoClient.getDatabase("galgame");
            var gameC = db.getCollection("game");
//            gameC.deleteMany("{}");
//            for (var d : gameC.find()) {
//                System.out.println(d);
//            }
            mongoClient.close();

        }
    }
}
