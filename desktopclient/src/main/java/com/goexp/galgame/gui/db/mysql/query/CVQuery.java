package com.goexp.galgame.gui.db.mysql.query;

import com.goexp.common.db.mysql.DBQueryTemplate;
import com.goexp.galgame.common.model.CV;
import com.goexp.galgame.gui.db.ICVQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CVQuery extends DBQueryTemplate<CV> implements ICVQuery {

    @Override
    public List<CV> cvList() {


        try {
            return runner.query("SELECT *  FROM `cv`", resultSet -> {
                var list = new ArrayList<CV>();
                while (resultSet.next()) {
                    var cv = new CV();
                    cv.star = resultSet.getInt("star");
                    cv.name = resultSet.getString("name");
                    cv.nameStr = resultSet.getString("names");
                    list.add(cv);
                }

                return list;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }
}
