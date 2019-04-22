package com.goexp.common.db.mongo;

import org.bson.Document;

@FunctionalInterface
public interface ObjectCreator<T> {
    T create(Document doc);
}
