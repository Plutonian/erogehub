package com.goexp.galgame.data.db.query;

import com.goexp.common.db.mysql.DBOperatorTemplate;
import com.goexp.galgame.common.model.CV;
import com.goexp.galgame.data.model.Game;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GameService extends DBOperatorTemplate {

    private final Logger logger = LoggerFactory.getLogger(GameService.class);

    private static String columns = "select g.* from game_copy1 as g";


    public Game get(int id) {

        logger.debug("<get> Game Id:{}", id);

        try {
            return runner.query(columns + " where g.id=?", (ResultSetHandler<Game>) resultSet -> {
                if (resultSet.next()) {
                    return createGame(resultSet);
                }

                return null;
            }, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Game> listByBrand(int brandId) {

        logger.debug("<listByBrand> Brand Id:{}", brandId);

        try {
            return runner.query(columns + " where g.brandId=? order by g.publishDate desc", (ResultSetHandler<List<Game>>) resultSet -> {
                var list = new ArrayList<Game>();
                while (resultSet.next()) {
                    Game g = createGame(resultSet);

                    list.add(g);
                }

                return list;

            }, brandId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Game createGame(ResultSet resultSet) throws SQLException {
        var g = new Game();

        g.id = resultSet.getInt("id");
        g.name = resultSet.getString("name");
        g.smallImg = resultSet.getString("smallimg");
        g.publishDate = Optional
                .ofNullable(resultSet.getDate("publishDate"))
                .map(Date::toLocalDate).orElse(null);
        g.story = resultSet.getString("story");
        g.painter = Arrays.asList(resultSet.getString("painter").split("、"));
        g.writer = Arrays.asList(resultSet.getString("writer").split("、"));
        g.tag = Arrays.asList(resultSet.getString("tag").split("、"));
        g.type = Arrays.asList(resultSet.getString("type").split("、"));
//        g.hash = resultSet.getString("hash");
        return g;
    }

    public List<Integer> idsByBrand(int brandId) {
        logger.debug("<idsByBrand> Brand Id:{}", brandId);

        try {
            return runner.query("select id from game_copy1 where brandid=?", (ResultSetHandler<ArrayList<Integer>>) resultSet -> {
                var ids = new ArrayList<Integer>();
                while (resultSet.next()) {
                    ids.add(resultSet.getInt("id"));
                }

                return ids;

            }, brandId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Integer gameSizeByBrand(int brandId) {
        logger.debug("<gameSizeByBrand> Brand Id:{}", brandId);

        try {
            return runner.query("select count(id) from game_copy1 where brandid=?", (ResultSetHandler<Integer>) resultSet -> {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }

                return 0;

            }, brandId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Integer> idsAll() {

        logger.debug("<idsAll>");

        try {
            return runner.query("select id from game_copy1", (ResultSetHandler<ArrayList<Integer>>) resultSet -> {
                var ids = new ArrayList<Integer>();
                while (resultSet.next()) {
                    ids.add(resultSet.getInt("id"));
                }

                return ids;

            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<CV> cvList() {


        try {
            return runner.query("SELECT *  FROM `cv`", resultSet -> {
                var list = new ArrayList<CV>();
                while (resultSet.next()) {
                    var cv = new CV();
                    cv.star = resultSet.getInt("star");
                    cv.name = resultSet.getString("name");
                    cv.nameStr = resultSet.getString("names");
//                    var name = resultSet.getString("name");
//                    var names = resultSet.getString("names");
                    list.add(cv);
                }

                return list;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public List<Integer> idsFrom(LocalDate start, LocalDate end) {

        logger.debug("<idsFrom>");

        try {
            return runner.query("select id from game_copy1 where publishdate between ? and ?", (ResultSetHandler<ArrayList<Integer>>) resultSet -> {
                var ids = new ArrayList<Integer>();
                while (resultSet.next()) {
                    ids.add(resultSet.getInt("id"));
                }

                return ids;

            }, start.toString(), end.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
