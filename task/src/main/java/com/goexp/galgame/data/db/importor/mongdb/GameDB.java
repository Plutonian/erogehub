package com.goexp.galgame.data.db.importor.mongdb;

import com.goexp.common.db.mongo.DBOperatorTemplate;
import com.goexp.common.db.mongo.DBQueryTemplate;
import com.goexp.galgame.data.model.Game;
import org.bson.Document;

import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class GameDB {
    static DBOperatorTemplate tlp = new DBOperatorTemplate("galgame", "game");
    DBQueryTemplate qlp = new DBQueryTemplate<Game>("galgame", "game", doc -> null);

    public void insert(Game game) {

        var gameDoc = new Document("_id", game.id)
                .append("name", game.name)
                .append("publishDate", game.publishDate)
                .append("smallImg", game.smallImg)
                .append("state", 0)
                .append("star", 0)
                .append("state", game.state.getValue())
                .append("brandId", game.brandId);

        tlp.exec(documentMongoCollection -> {
            documentMongoCollection.insertOne(gameDoc);
        });

    }

    public void update(Game game) {

        tlp.exec(documentMongoCollection -> {
            documentMongoCollection.updateOne(eq("_id", game.id),
                    combine(
                            set("publishDate", game.publishDate)
                            , set("smallImg", game.smallImg)
                    ));
        });
    }

    public void updateAll(Game game) {

        tlp.exec(documentMongoCollection -> {
            documentMongoCollection.updateOne(eq("_id", game.id),
                    combine(
                            set("painter", game.painter)
                            , set("writer", game.writer)
                            , set("type", game.type)
                            , set("tag", game.tag)
                            , set("story", game.story)
                            , set("intro", game.intro)
                            , set("brandId", game.brandId)
                    ));
        });
    }

    public void updateChar(Game game) {

        var gameCharDocs = game.gameCharacterList
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

        tlp.exec(documentMongoCollection -> {
            documentMongoCollection.updateOne(eq("_id", game.id),
                    set("gamechar", gameCharDocs));
        });
    }

    public void updateImg(Game game) {

        var imgdocs = game.imgList.stream()
                .map(img -> {
                    var doc = new Document("src", img.src)
                            .append("index", img.index);

                    return doc;
                })
                .collect(Collectors.toList());

        tlp.exec(documentMongoCollection -> {
            documentMongoCollection.updateOne(eq("_id", game.id),
                    set("simpleImg", imgdocs));
        });
    }

    public boolean exist(int id) {
        return qlp.exists(eq("_id", id));
    }

    public static class StateDB {
        public void update(Game game) {
            tlp.exec(documentMongoCollection -> {
                documentMongoCollection.updateOne(
                        eq("_id", game.id)
                        , set("state", game.state.getValue())
                );
            });
        }
    }

}
