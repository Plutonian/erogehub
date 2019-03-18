package com.goexp.galgame.data.db.importor.mongdb;

import com.goexp.common.db.mongo.DBOperatorTemplate;
import com.goexp.common.db.mysql.DBUpdateTemplate;
import com.goexp.galgame.data.model.Brand;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class BrandDB extends DBUpdateTemplate {

    public static DBOperatorTemplate tlp = new DBOperatorTemplate("galgame", "brand");

    public void insert(Brand item) {

        var doc = new Document("_id", item.id)
                .append("name", item.name)
                .append("website", item.website)
                .append("type", 0);


        tlp.exec(gameC -> {
            gameC.insertOne(doc);

        });

    }

    public void updateWebsite(Brand item) {

        tlp.exec(documentMongoCollection -> {
            documentMongoCollection.updateOne(eq(item.id), set("website", item.website));
        });
    }

    public void updateComp(Brand item) {

        tlp.exec(documentMongoCollection -> {
            documentMongoCollection.updateOne(eq(item.id),
                    set("comp", item.comp));
        });
    }

}
