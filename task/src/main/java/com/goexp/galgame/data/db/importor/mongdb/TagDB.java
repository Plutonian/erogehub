package com.goexp.galgame.data.db.importor.mongdb;

import com.goexp.common.db.mongo.DBOperatorTemplate;
import com.goexp.common.db.mysql.DBUpdateTemplate;
import com.goexp.galgame.data.model.Tag;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

public class TagDB extends DBUpdateTemplate {

    DBOperatorTemplate tlp = new DBOperatorTemplate("galgame", "tag");

    public void insert(List<Tag.TagType> item) {


        var docs = item.stream()
                .map(tagType -> {
                    var gameDoc = new Document("type", tagType.type)
                            .append("order", tagType.order);
                    gameDoc.append("tags", tagType.tags);


                    return gameDoc;
                })
                .collect(Collectors.toList());

        tlp.exec(gameC -> {
            gameC.insertMany(docs);

        });

    }

}
