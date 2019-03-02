package com.goexp.common.db.mysql;

import java.sql.SQLException;

public class DBUpdateTemplate extends DBQueryTemplate {

    public void exec(String sql,Object... params)
    {
        try {
            runner.execute(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
