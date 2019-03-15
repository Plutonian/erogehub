package com.goexp.galgame.gui.db.mongo.query;

import com.goexp.common.db.mongo.DBQueryTemplate;
import com.goexp.common.db.mongo.ObjectCreator;
import com.goexp.galgame.common.model.TagType;
import org.bson.Document;

import java.util.List;

public class TagQuery {

    public final static DBQueryTemplate<TagType> tlp = new DBQueryTemplate.Builder<>("galgame", "tag", new TagCreator())
            .build();

    private static class TagCreator implements ObjectCreator<TagType> {
        @Override
        public TagType create(Document doc) {
            var t = new TagType();
            t.type = doc.getString("type");
            t.order = doc.getInteger("order");
            t.tags = (List<String>) doc.get("tags", List.class);
            return t;
        }
    }

}
