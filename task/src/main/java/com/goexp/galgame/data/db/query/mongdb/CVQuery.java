package com.goexp.galgame.data.db.query.mongdb;

import com.goexp.common.db.mongo.DBQueryTemplate;
import com.goexp.common.db.mongo.ObjectCreator;
import com.goexp.galgame.common.model.CV;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CVQuery {

    public static final DBQueryTemplate<CV> tlp = new DBQueryTemplate.Builder<>("galgame", "cv", new CVCreator()).build();

    private static class CVCreator implements ObjectCreator<CV> {

        private final Logger logger = LoggerFactory.getLogger(CVCreator.class);

        @Override
        public CV create(Document doc) {
            var g = new CV();

            logger.debug("<create> doc={}", doc);

            g.name = doc.getString("name");
            g.star = doc.getInteger("star");
            g.nameStr = doc.getString("names");
            return g;
        }
    }
}
