package com.goexp.galgame.gui.db.mongo;

import com.goexp.common.db.mongo.DBOperatorTemplate;
import com.goexp.galgame.gui.model.Brand;

import java.sql.SQLException;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class BrandDB {

    private static DBOperatorTemplate tlp = new DBOperatorTemplate("galgame", "brand");

    public void updateWebsite(Brand item) {

        tlp.exec(documentMongoCollection -> {
            documentMongoCollection.updateOne(
                    eq("_id", item.id)
                    , set("website", item.website)
            );
        });
    }


    public void updateIsLike(Brand item) {

        tlp.exec(documentMongoCollection -> {
            documentMongoCollection.updateOne(
                    eq("_id", item.id)
                    , set("type", item.isLike.getValue())
            );
        });
    }

}
