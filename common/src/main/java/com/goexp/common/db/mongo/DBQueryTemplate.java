package com.goexp.common.db.mongo;

import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DBQueryTemplate<T> extends AbstractDBTemplate {

    private Bson defaultSort;

    private Bson defaultSelect;

    private final ObjectCreator<T> defaultCreator;

    private DBQueryTemplate(String dbName, String tableName, ObjectCreator<T> defaultCreator) {
        super(dbName, tableName);

        Objects.requireNonNull(defaultCreator);
        this.defaultCreator = defaultCreator;
    }

    private DBQueryTemplate(String dbName, String tableName, ObjectCreator<T> defaultCreator, Bson defaultSelect, Bson defaultSort) {
        this(dbName, tableName, defaultCreator);

        this.defaultSelect = defaultSelect;
        this.defaultSort = defaultSort;
    }

    public QueryBuilder query() {
        return this.new QueryBuilder();
    }


    public class QueryBuilder {

        private QueryBuilder() {
        }

        private Bson where;

        private Bson select;

        private Bson sort;


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
            var db = mongoClient.getDatabase(dbName);
            var collection = db.getCollection(tableName);

            var temp = where != null ? collection.find(where) : collection.find();

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

            var docs = temp;

            var list = new ArrayList<T>();
            for (var doc : docs) {
                list.add((T) defaultCreator.create(doc));
            }
            return list;
        }

        public T one() {
            var db = mongoClient.getDatabase(dbName);
            var collection = db.getCollection(tableName);

            var temp = where != null ? collection.find(where) : collection.find();
            var docs = temp;

            return docs.first() != null ? (T) defaultCreator.create(docs.first()) : null;
        }

        public boolean exists() {
            var db = mongoClient.getDatabase(dbName);
            var collection = db.getCollection(tableName);

            var temp = where != null ? collection.find(where) : collection.find();
            var docs = temp;

            return docs.first() != null;
        }

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

        public Builder defaultSort(Bson defaultSort) {
            this.defaultSort = defaultSort;
            return this;
        }

        public Builder defaultSelect(Bson defaultSelect) {
            this.defaultSelect = defaultSelect;
            return this;
        }

        public DBQueryTemplate<T> build() {
            return new DBQueryTemplate<>(dbName, tableName, creator, defaultSelect, defaultSort);
        }
    }
}
