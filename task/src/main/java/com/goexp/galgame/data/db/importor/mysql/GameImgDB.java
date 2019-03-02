package com.goexp.galgame.data.db.importor.mysql;

import com.goexp.common.db.mysql.DBOperatorTemplate;
import com.goexp.galgame.data.model.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameImgDB extends DBOperatorTemplate {


    public void insert(Game.Img img) {
        try {
            runner.update("insert into img ()values (?,?,?,?)", img.id, img.src, img.gameId, img.index);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    public void batchInsertImg(List<Game.Img> imgs) {
//        try {
//
//            var param = new Object[imgs.size()][];
//
//            for (var i = 0; i < imgs.size(); i++) {
//                var img = imgs.get(i);
//                param[i] = new Object[]{img.id, img.src, img.gameId, img.index};
//            }
//
//
//            runner.batch("insert into img ()values (?,?,?,?)", param);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean exist(String id) {
        try {

            return runner.query("select 1 from img where id=? limit 1", ResultSet::next, id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
