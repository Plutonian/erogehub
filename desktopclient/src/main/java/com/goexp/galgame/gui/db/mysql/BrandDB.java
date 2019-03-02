package com.goexp.galgame.gui.db.mysql;

import com.goexp.common.db.mysql.DBOperatorTemplate;
import com.goexp.galgame.gui.model.Brand;

import java.sql.SQLException;

public class BrandDB extends DBOperatorTemplate {

    public void updateWebsite(Brand item) {
        try {

            runner.update("update getchu_brand set website=? where id=?"
                    , item.website
                    , item.id
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateIsLike(Brand item) {
        try {

            runner.update("update getchu_brand set isLike=? where id=?"
                    , item.isLike.getValue()
                    , item.id
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
