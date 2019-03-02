package com.goexp.galgame.gui.db.mysql;

import com.goexp.common.db.mysql.DBOperatorTemplate;
import com.goexp.galgame.gui.model.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SeriesDB extends DBOperatorTemplate {

    public void insert(String id, int brandId, String name) {

        try {

            runner.update("insert into series ()values (?,?,?)"
                    , id
                    , brandId
                    , name
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGame(String sid, List<Game> games) {
        var param = new Object[games.size()][];

        for (var i = 0; i < games.size(); i++) {
            var game = games.get(i);
            param[i] = new Object[]{sid, game.id};
        }

        try {
            runner.batch("insert into series_game () values (?,?)", param);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean exist(String id) {
        try {

            return runner.query("select 1 from series where id=? limit 1", ResultSet::next, id);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
