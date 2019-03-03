package com.goexp.galgame.data.db.importor.mongdb;

import com.goexp.common.db.mongo.DBOperatorTemplate;
import com.goexp.common.db.mysql.DBUpdateTemplate;
import com.goexp.galgame.data.model.Game;
import org.bson.Document;

public class GuideDB extends DBUpdateTemplate {

    DBOperatorTemplate tlp = new DBOperatorTemplate("galgame", "guide");

    public void insert(Game.Guide item) {


        var doc = new Document("_id", item.id)
                .append("title", item.title)
                .append("href", item.href)
//                .append("content", item.content)
                .append("from", item.from.getValue());


        tlp.exec(gameC -> {
            gameC.insertOne(doc);

        });

    }

}
