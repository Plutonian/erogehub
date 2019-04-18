package com.goexp.common.db.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public abstract class AbstractDBTemplate {

    protected static MongoClient mongoClient;

    static {
        mongoClient = MongoClients.create();
    }

    protected final String dbName;
    protected final String tableName;

    public AbstractDBTemplate(String dbName, String tableName) {
        this.dbName = dbName;
        this.tableName = tableName;
    }
}
