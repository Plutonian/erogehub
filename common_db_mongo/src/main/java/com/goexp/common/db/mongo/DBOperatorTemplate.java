package com.goexp.common.db.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Objects;
import java.util.function.Consumer;

public class DBOperatorTemplate extends AbstractDBTemplate {

    public DBOperatorTemplate(String dbName, String tableName) {
        super(dbName, tableName);
        Objects.requireNonNull(dbName);
        Objects.requireNonNull(tableName);
    }

    public void exec(Consumer<MongoCollection<Document>> back) {

        Objects.requireNonNull(back);

        var db = mongoClient.getDatabase(dbName);
        back.accept(db.getCollection(tableName));

    }

}
