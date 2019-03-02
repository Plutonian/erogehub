package com.goexp.common.db.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ObjectCreator<T> {
    T create(ResultSet resultSet) throws SQLException;
}
