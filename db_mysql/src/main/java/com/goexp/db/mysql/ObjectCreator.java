package com.goexp.db.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ObjectCreator<T> {
    T create(ResultSet resultSet) throws SQLException;
}
