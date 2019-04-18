package com.goexp.common.db.mysql;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBQueryTemplate<T> extends DBOperatorTemplate<T> {

    protected final List<T> getList(ObjectCreator<T> creator, String sql, Object... params) {

        var list = new ArrayList<T>();

        try {

            return getRunner().query(sql,
                    (ResultSetHandler<List<T>>) resultSet -> {
                        while (resultSet.next()) {
                            list.add(creator.create(resultSet));
                        }

                        return list;

                    }, params);


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    protected final T get(ObjectCreator<T> creator, String sql, Object... params) {

        try {
            return getRunner().query(sql,
                    (ResultSetHandler<T>) resultSet -> {
                        if (resultSet.next()) {
                            return creator.create(resultSet);
                        }

                        return null;
                    }, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
