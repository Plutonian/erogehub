package com.goexp.galgame.gui.db.mysql.query;

import com.goexp.common.db.mysql.DBOperatorTemplate;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.model.Series;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeriesQuery extends DBOperatorTemplate {

    private final Logger logger = LoggerFactory.getLogger(SeriesQuery.class);

    public List<Series> list(int brandid) {


        var list = new ArrayList<Series>();

        try {
            return runner.query("select * from series where brandid=? order by name", (ResultSetHandler<List<Series>>) resultSet -> {
                while (resultSet.next()) {
                    Series series = new Series();
                    series.id = resultSet.getString("id");
                    series.name = resultSet.getString("name");
                    series.brandId = brandid;

                    list.add(series);
                }

                return list;

            }, brandid);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void fill(Series series) {

        series.games = new ArrayList<>();

        try {
            runner.query("select g.id,g.smallImg from series_game as s inner join game_copy1 as g on s.gameid=g.id where s.seriesid=? order by g.publishdate desc", (ResultSetHandler<List<Series>>) resultSet -> {
                while (resultSet.next()) {
                    //                Series series = new Series();
                    var game = new Game();
                    game.id = resultSet.getInt("id");
                    game.smallImg = resultSet.getString("smallImg");

                    series.games.add(game);

                }

                return null;

            }, series.id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
