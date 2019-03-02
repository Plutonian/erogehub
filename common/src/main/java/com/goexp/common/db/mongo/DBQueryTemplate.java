package com.goexp.common.db.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class DBQueryTemplate<T> extends DBOperatorTemplate {

    private final ObjectCreator<T> creator;

    public DBQueryTemplate(String dbName, String tableName, ObjectCreator<T> creator) {
        super(dbName, tableName);

        Objects.requireNonNull(creator);
        this.creator = creator;
    }


    public boolean exists(Bson filters) {
        var db = mongoClient.getDatabase(dbName);
        var collection = db.getCollection(tableName);
        var docs = collection.find(filters);

        return docs.first() != null;
    }

    public T one(Bson filters) {

        var db = mongoClient.getDatabase(dbName);
        var collection = db.getCollection(tableName);
        var docs = collection.find(filters);

        return docs.first() != null ? creator.create(docs.first()) : null;
    }

    public T one(Bson filters, Bson select) {

        var db = mongoClient.getDatabase(dbName);
        var collection = db.getCollection(tableName);
        var docs = collection.find(filters).projection(select);

        return docs.first() != null ? creator.create(docs.first()) : null;
    }


    public List<T> list(Bson where, Bson select,Bson sort) {

        Objects.requireNonNull(where);
        Objects.requireNonNull(select);
        Objects.requireNonNull(sort);

        var db = mongoClient.getDatabase(dbName);
        var collection = db.getCollection(tableName);

        var docs = collection.find(where).projection(select).sort(sort);

        var list = new ArrayList<T>();
        for (var doc : docs) {
            list.add(creator.create(doc));
        }
        return list;


    }

    public List<T> list(Bson where, Bson select) {

        Objects.requireNonNull(where);
        Objects.requireNonNull(select);

        var db = mongoClient.getDatabase(dbName);
        var collection = db.getCollection(tableName);

        var docs = collection.find(where).projection(select);

        var list = new ArrayList<T>();
        for (var doc : docs) {
            list.add(creator.create(doc));
        }
        return list;


    }

    public List<T> list(Bson filters) {
        Objects.requireNonNull(filters);

        var db = mongoClient.getDatabase(dbName);
        var collection = db.getCollection(tableName);

        var docs = collection.find(filters);

        var list = new ArrayList<T>();
        for (var doc : docs) {
            list.add(creator.create(doc));
        }
        return list;


    }

    public List<T> list() {

        var db = mongoClient.getDatabase(dbName);
        var collection = db.getCollection(tableName);
        var docs = collection.find();

        var list = new ArrayList<T>();
        for (var doc : docs) {
            list.add(creator.create(doc));
        }
        return list;
    }

    public List<T> list(Function<MongoCollection<Document>, FindIterable<Document>> back) {
        Objects.requireNonNull(back);

        var db = mongoClient.getDatabase(dbName);
        var collection = db.getCollection(tableName);
        var docs = back.apply(collection);

        var list = new ArrayList<T>();
        for (var doc : docs) {
            list.add(creator.create(doc));
        }
        return list;
    }

}
