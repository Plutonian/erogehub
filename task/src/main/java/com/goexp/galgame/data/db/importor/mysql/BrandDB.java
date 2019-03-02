package com.goexp.galgame.data.db.importor.mysql;

import com.goexp.common.db.mysql.DBUpdateTemplate;
import com.goexp.galgame.data.model.Brand;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BrandDB extends DBUpdateTemplate {

    public void insert(Brand item) {

        exec("insert into getchu_brand (id,`name`,website)values (?,?,?,?)",
                item.id
                , item.name
                , item.website
        );

    }

//    public void update(Brand item) {
//
//        exec("update getchu_brand set name=?,website=?,isMain=? where id=?"
//                , item.name
//                , item.website
//                , item.isMain ? 1 : 0
//                , item.id
//        );
//    }

//    public void updateIsMain(String title) {
//        exec("update getchu_brand set isMain=1 where name=?"
//                , title
//        );
//    }
//
//    public void updateIsLike(Brand item) {
//        exec("update getchu_brand set isLike=? where id=?"
//                , item.isLike
//                , item.id
//        );
//    }

    public void updateWebsite(Brand item) {

        exec("update getchu_brand set website=? where id=?"
                , item.website
                , item.id
        );
    }

//    public void updateComp(Brand item) {
//
//        exec("update getchu_brand set comp=? where id=?"
//                , item.comp
//                , item.id
//        );
//    }

//    public boolean exist(int id) {
//        try {
//
//            return runner.query("select 1 from getchu_brand where id=? limit 1", ResultSet::next, id);
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

}
