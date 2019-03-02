package com.goexp.galgame.gui.db.mongo.query;

import com.goexp.common.db.mongo.DBQueryTemplate;
import com.goexp.common.db.mongo.ObjectCreator;
import com.goexp.galgame.gui.model.Tag;
import org.bson.Document;

import java.util.List;

import static com.mongodb.client.model.Sorts.ascending;

public class TagQuery {

    private DBQueryTemplate<Tag.TagType> tlp = new DBQueryTemplate<>("galgame", "tag", new TagCreator());

    public List<Tag.TagType> types() {

        return tlp.list(documentMongoCollection -> {
            return documentMongoCollection.find().sort(ascending("order"));
        });
    }

    private static class TagCreator implements ObjectCreator<Tag.TagType> {
        @Override
        public Tag.TagType create(Document doc) {
            var t = new Tag.TagType();
            t.type = doc.getString("type");
            t.order = doc.getInteger("order");
            t.tags = (List<String>) doc.get("tags", List.class);
            return t;
        }
    }

    public static void main(String[] args) {
        new TagQuery().types()
                .forEach(tagType -> {

                    System.out.println(tagType);
                    tagType.tags.forEach(System.out::println);
                });
    }
}
