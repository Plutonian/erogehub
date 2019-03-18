package com.goexp.common.db.mysql;

import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class DBOperatorTemplate<T> {


    private static QueryRunner runner;

    QueryRunner getRunner() {
        if (runner == null) {
            try {
                var prop = ResourceBundle.getBundle("dbconfig");
                var unpooled = DataSources.unpooledDataSource(prop.getString("dbString"));
                var pooled = DataSources.pooledDataSource(unpooled);

                runner = new QueryRunner(pooled);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return runner;
    }


}
