package com.goexp.galgame.data.db.query.mongdb;

import com.goexp.common.db.mongo.DBQueryTemplate;
import com.goexp.common.db.mongo.ObjectCreator;
import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.data.model.Brand;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public class BrandQuery {

    private final Logger logger = LoggerFactory.getLogger(BrandQuery.class);

    private DBQueryTemplate<Brand> tlp = new DBQueryTemplate<>("galgame","brand",new BrandCreator());

    public Brand getById(int id) {
        return (Brand) tlp.one(eq("_id", id));
    }

    public List<Brand> list() {
        logger.debug("<list>");

        return tlp.list();

    }

    public List<Brand> list(int type) {
        logger.debug("<list> type={}", type);

        return tlp.list(eq("type", type));

    }

    public List<Brand> listByName(String keyword) {
        logger.debug("<listByName> keyword={}", keyword);

        return tlp.list(regex("name", "^" + keyword));
    }

    public List<Brand> listByComp(String comp) {

        logger.debug("<listByComp> comp={}", comp);

        return tlp.list(eq("comp", comp));

    }

    public List<Brand> queryByComp(String keyword) {
        logger.debug("<queryByComp> keyword={}", keyword);

        return tlp.list(regex("comp", keyword));

    }


    private static class BrandCreator implements ObjectCreator<Brand> {

        private final Logger logger = LoggerFactory.getLogger(BrandCreator.class);

        @Override
        public Brand create(Document doc) {
            var g = new Brand();

            logger.debug("<create> doc={}", doc);

            g.id = doc.getInteger("_id");
            g.name = doc.getString("name");
            g.website = doc.getString("website");
//            g.isMain = doc.getBoolean("isMain");
//            g.isLike = BrandType.from(doc.getInteger("type"));
            g.comp = doc.getString("comp");
//            g.index = doc.getString("index");
//            g.dead = doc.getBoolean("dead");
            return g;
        }
    }
}
