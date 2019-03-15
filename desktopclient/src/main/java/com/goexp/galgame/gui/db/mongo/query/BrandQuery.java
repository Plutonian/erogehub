package com.goexp.galgame.gui.db.mongo.query;

import com.goexp.common.db.mongo.DBQueryTemplate;
import com.goexp.common.db.mongo.ObjectCreator;
import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.gui.model.Brand;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrandQuery {

    public final static DBQueryTemplate<Brand> tlp = new DBQueryTemplate.Builder<>("galgame", "brand", new BrandCreator())
            .build();

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
