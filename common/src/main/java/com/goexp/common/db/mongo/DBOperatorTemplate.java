package com.goexp.common.db.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Objects;
import java.util.function.Consumer;

public class DBOperatorTemplate {

    static {
        mongoClient = MongoClients.create();
    }

    protected static MongoClient mongoClient;

    protected final String dbName;

    protected final String tableName;

    public DBOperatorTemplate(String dbName, String tableName) {
        this.dbName = dbName;
        this.tableName = tableName;
    }

    public void exec(Consumer<MongoCollection<Document>> back) {

        Objects.requireNonNull(back);

        var db = mongoClient.getDatabase(dbName);
        back.accept(db.getCollection(tableName));

    }

}
