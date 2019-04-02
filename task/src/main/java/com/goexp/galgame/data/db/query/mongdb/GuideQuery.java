package com.goexp.galgame.data.db.query.mongdb;

import com.goexp.common.db.mongo.DBQueryTemplate;
import com.goexp.common.db.mongo.ObjectCreator;
import com.goexp.galgame.common.model.CommonGame;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuideQuery {

    public static DBQueryTemplate<CommonGame.Guide> tlp = new DBQueryTemplate.Builder<>("galgame", "guide", new GuideQuery.Creator()).build();

    private static class Creator implements ObjectCreator<CommonGame.Guide> {

        private final Logger logger = LoggerFactory.getLogger(GuideQuery.Creator.class);

        @Override
        public CommonGame.Guide create(Document doc) {
            var g = new CommonGame.Guide();

            logger.debug("<create> doc={}", doc);

            g.id = doc.getString("_id");
            g.href = doc.getString("name");
            g.title = doc.getString("title");
            g.from = CommonGame.Guide.DataFrom.from(doc.getInteger("from"));
            return g;
        }
    }
}
