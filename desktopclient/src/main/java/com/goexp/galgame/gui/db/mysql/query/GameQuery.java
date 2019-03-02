package com.goexp.galgame.gui.db.mysql.query;

import com.goexp.common.db.mysql.DBOperatorTemplate;
import com.goexp.common.db.mysql.DBQueryTemplate;
import com.goexp.common.db.mysql.ObjectCreator;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.db.IGameQuery;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
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
import java.util.stream.Collectors;

public class GameQuery extends DBQueryTemplate<Game> implements IGameQuery {

    private static String columns = "select g.id,g.`name`, g.brandId,g.publishDate,g.story,g.intro,g.smallimg,g.painter,g.writer,g.tag,g.type,t.state,s.star from game_copy1 as g" +
            " inner join game_star as s on g.id=s.id" +
            " inner join game_state as t on g.id=t.id" +
            " inner join getchu_brand as b on g.brandid=b.id";



    private final Logger logger = LoggerFactory.getLogger(GameQuery.class);


    private List<Game> getList(String sql, Object... params) {
        return super.getList(new GameCreator(), sql, params);
    }


    @Override
    public List<Game> list(GameState gameState) {

        return getList(columns + " where t.state=? order by g.publishDate desc"
                , gameState.getValue());
    }

    @Override
    public List<Game> listByStarRange(int begin, int end) {

        return getList(columns + " where s.star between ? and ? order by g.publishDate desc"
                , begin
                , end);
    }

    @Override
    public List<Game> listByBrand(int brandId) {

        return getList(columns + " where g.brandId=? order by g.publishDate desc"
                , brandId
        );
    }

    @Override
    public List<Game> list(int brandId, GameState gameState) {

        return getList(columns + " where g.brandId=? and t.state=? order by g.publishDate desc"
                , brandId
                , gameState.getValue()
        );

    }

    @Override
    public List<Game> list(LocalDate start, LocalDate end) {

        return getList(columns + " where  g.publishDate between ? and ? order by g.publishDate desc"
//                , GameState.BLOCK.getValue()
                , start.toString()
                , end.toString()
        );

    }

    public static class Export extends DBQueryTemplate<Game> {

        private List<Game> getList(String sql, Object... params) {
            return super.getList(new GameCreator(), sql, params);
        }

        public List<Game> list(LocalDate start, LocalDate end) {

            return getList(columns + " where g.publishDate between ? and ? order by g.publishDate desc,b.`order` desc,b.islike desc,s.star desc"
                    , start.toString()
                    , end.toString()
            );

        }

        public List<Game> listByDate(LocalDate end) {

            return getList(columns + " where g.publishDate <= ? order by g.publishDate desc,b.`order` desc,b.islike desc,s.star desc"
                    , end.toString()
            );

        }

    }


    /**
     * Search
     */


    @Override
    public List<Game> searchByTag(String tag) {

        return getList(columns + " where t.state>? and find_in_set(?,g.`tag`)>0 order by g.publishDate desc"
                , GameState.BLOCK.getValue()
                , tag
        );
    }


    @Override
    public List<Game> searchByName(String keyword) {
        return getList(columns + " where t.state>? and g.`name` like ? order by g.publishDate desc"
                , GameState.SAME.getValue()
                , keyword + "%"
        );
    }

    @Override
    public List<Game> searchByNameEx(String keyword) {
        return getList(columns + " where t.state>? and g.`name` like ? order by g.publishDate desc"
                , GameState.SAME.getValue()
                , "%" + keyword + "%"
        );
    }

    @Override
    public List<Game> searchByPainter(String keyword) {

        return getList(columns + " where t.state>? and find_in_set(?,g.`painter`)>0 order by g.publishDate desc"
                , GameState.SAME.getValue()
                , keyword
        );
    }

    @Override
    public List<Game> queryByCV(String keyword) {

        return getList(columns + " inner join `character` as c on c.gameid=g.id where t.state>? and c.trueCV=? order by g.publishDate desc"
                , GameState.SAME.getValue()
                , keyword
        ).stream()
                .distinct()
                .collect(Collectors.toList());

    }

    public static class GameCreator implements ObjectCreator<Game> {


        public Game create(ResultSet resultSet) throws SQLException {
            var g = new Game();

            g.id = resultSet.getInt("id");
            g.name = resultSet.getString("name");
            g.brand = new Brand();
            g.brand.id = resultSet.getInt("brandId");
            g.publishDate = Optional
                    .ofNullable(resultSet.getDate("publishDate"))
                    .map(Date::toLocalDate).orElse(null);
            g.smallImg = resultSet.getString("smallimg");
            g.painter = str2list(resultSet.getString("painter"));
            g.writer = str2list(resultSet.getString("writer"));
            g.tag = str2list(resultSet.getString("tag"));
            g.type = str2list(resultSet.getString("type"));
            g.setState(GameState.from(resultSet.getInt("state")));
            g.star = resultSet.getInt("star");
            g.intro = resultSet.getString("intro");
            g.story = resultSet.getString("story");
            return g;
        }

        private List<String> str2list(String str) {
            return Arrays.stream(str.split(","))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

        }
    }

    public static class GameImgQuery extends DBQueryTemplate<Game.GameImg> {

        public List<Game.GameImg> list(int gameId) {

            try {
                return runner.query("select src,`index` from img where gameId=? order by `index` asc", (ResultSetHandler<List<Game.GameImg>>) resultSet -> {
                    var list = new ArrayList<Game.GameImg>();
                    while (resultSet.next()) {
                        var g = new Game.GameImg();
//                        g.id = resultSet.getString("id");
                        g.src = resultSet.getString("src");
                        g.index = resultSet.getInt("index");
//                        g.gameId = gameId;
                        list.add(g);
                    }

                    return list;

                }, gameId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    public static class GameCharQuery extends DBOperatorTemplate {

        private final Logger logger = LoggerFactory.getLogger(GameCharQuery.class);


        //    public GameCharQuery() {
        //        load();
        //    }
        //
        //    private void load() {
        //        var dataSource = new MysqlDataSource();
        //        dataSource.setUrl(Config.dbString);
        //
        //        runner = new QueryRunner(dataSource);
        //
        //    }

        public List<Game.GameCharacter> list(int gameId) {


            var list = new ArrayList<Game.GameCharacter>();

            try {
                return runner.query("select * from `character` where gameId=? order by `index` asc", (ResultSetHandler<List<Game.GameCharacter>>) resultSet -> {
                    while (resultSet.next()) {
                        var g = new Game.GameCharacter();

                        g.id = resultSet.getString("id");
                        g.name = resultSet.getString("name");
                        g.cv = resultSet.getString("cv");
                        g.intro = resultSet.getString("intro");
                        g.gameId = gameId;
                        g.trueCV = resultSet.getString("trueCV");
                        g.img = resultSet.getString("img");
                        g.index = resultSet.getInt("index");

                        list.add(g);
                    }

                    return list;

                }, gameId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
