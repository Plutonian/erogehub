package com.goexp.common.db.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DBQueryTemplate<T> extends AbstractDBTemplate {

    private final MongoCollection<Document> collection;
    private final ObjectCreator<T> defaultCreator;
    private Bson defaultSort;
    private Bson defaultSelect;

    private DBQueryTemplate(String dbName, String tableName, ObjectCreator<T> defaultCreator) {
        super(dbName, tableName);

        Objects.requireNonNull(defaultCreator);
        this.defaultCreator = defaultCreator;

        var db = mongoClient.getDatabase(dbName);
        collection = db.getCollection(tableName);
    }

    private DBQueryTemplate(String dbName, String tableName, ObjectCreator<T> defaultCreator, Bson defaultSelect, Bson defaultSort) {
        this(dbName, tableName, defaultCreator);

        this.defaultSelect = defaultSelect;
        this.defaultSort = defaultSort;
    }

    public QueryBuilder query() {
        return this.new QueryBuilder();
    }

    public static class Builder<T> {
        private String dbName;
        private String tableName;
        private ObjectCreator<T> creator;
        private Bson defaultSort;
        private Bson defaultSelect;


        public Builder(String dbName, String tableName, ObjectCreator<T> creator) {
            this.dbName = dbName;
            this.tableName = tableName;
            this.creator = creator;
        }

        public Builder<T> defaultSort(Bson defaultSort) {
            this.defaultSort = defaultSort;
            return this;
        }

        public Builder<T> defaultSelect(Bson defaultSelect) {
            this.defaultSelect = defaultSelect;
            return this;
        }

        public DBQueryTemplate<T> build() {
            return new DBQueryTemplate<>(dbName, tableName, creator, defaultSelect, defaultSort);
        }
    }

    public class QueryBuilder {

        private Bson where;
        private Bson select;
        private Bson sort;

        private QueryBuilder() {
        }

        public QueryBuilder where(Bson where) {
            this.where = where;
            return this;
        }

        public QueryBuilder select(Bson select) {
            this.select = select;
            return this;
        }

        public QueryBuilder sort(Bson sort) {
            this.sort = sort;
            return this;
        }

        public List<T> list() {
            return getList(defaultCreator);
        }

        public List<T> list(ObjectCreator<T> userCreator) {
            Objects.requireNonNull(userCreator);

            return getList(userCreator);
        }

        public T one() {

            var temp = collection.find();

            // choice where
            if (where != null) {
                temp = temp.filter(where);
            }

            return temp.limit(1).map(defaultCreator::create).first();
        }

        public T one(ObjectCreator<T> userCreator) {
            Objects.requireNonNull(userCreator);

            var temp = collection.find();

            // choice where
            if (where != null) {
                temp = temp.filter(where);
            }

            return temp.limit(1).map(userCreator::create).first();
        }

        public boolean exists() {
            return collection.countDocuments(where) > 0;
        }

        private ArrayList<T> getList(ObjectCreator<T> userCreator) {
            return getDocuments().map(userCreator::create).into(new ArrayList<>());
        }

        private FindIterable<Document> getDocuments() {


            var temp = collection.find();

            // choice where
            if (where != null) {
                temp = temp.filter(where);
            }

            // choice select
            if (select != null)
                temp = temp.projection(select);
            else if (defaultSelect != null)
                temp = temp.projection(defaultSelect);

            // choice sort
            if (sort != null)
                temp = temp.sort(sort);
            else if (defaultSort != null)
                temp = temp.sort(defaultSort);

            return temp;
        }

    }
}
