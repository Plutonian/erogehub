package com.goexp.galgame.data.db.importor.mongdb;

import com.goexp.common.db.mongo.DBOperatorTemplate;
import com.goexp.common.db.mysql.DBUpdateTemplate;
import com.goexp.galgame.data.model.Brand;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class BrandDB extends DBUpdateTemplate {

    DBOperatorTemplate tlp = new DBOperatorTemplate("galgame", "brand");

    public void insert(Brand item) {

//        exec("insert into getchu_brand (id,`name`,website,`index`)values (?,?,?,?)",
//                item.id
//                , item.name
//                , item.website
//                , item.index
//        );

        var doc = new Document("_id", item.id)
                .append("name", item.name)
                .append("website", item.website)
                .append("type", 0);


        tlp.exec(gameC -> {
            gameC.insertOne(doc);

        });

    }

//    public void update(Brand item) {
//
//        exec("update getchu_brand set name=?,website=?,isMain=? where id=?"
//                , item.name
//                , item.website
//                , item.isMain ? 1 : 0
//                , item.id
//        );
//    }

//    public void updateIsMain(String title) {
//        exec("update getchu_brand set isMain=1 where name=?"
//                , title
//        );
//    }
//
//    public void updateIsLike(Brand item) {
//        exec("update getchu_brand set isLike=? where id=?"
//                , item.isLike
//                , item.id
//        );
//    }

    public void updateWebsite(Brand item) {

        tlp.exec(documentMongoCollection -> {
            documentMongoCollection.updateOne(eq("_id", item.id), set("website", item.website));
        });
    }

    public void updateComp(Brand item) {

        tlp.exec(documentMongoCollection -> {
            documentMongoCollection.updateOne(eq("_id", item.id),
                    set("comp", item.comp));
        });
    }

//    public boolean exist(int id) {
//        try {
//
//            return runner.query("select 1 from getchu_brand where id=? limit 1", ResultSet::next, id);
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

}
