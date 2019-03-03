package com.goexp.galgame.data.db.query.mysql;

import com.goexp.common.db.mysql.DBQueryTemplate;
import com.goexp.common.db.mysql.ObjectCreator;
import com.goexp.galgame.data.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameCharService extends DBQueryTemplate<Game.GameCharacter> {

    private static final ObjectCreator<Game.GameCharacter> creator = new GameCharCreator();

    private final Logger logger = LoggerFactory.getLogger(GameCharService.class);


    public List<Game.GameCharacter> listByGame(int gameId) {

        return getList(creator, "select * from `character` where gameId=?", gameId);

    }

    public List<Game.GameCharacter> listByCV() {
        return getList(creator, "select id,`name`,cv from `character` where `cv`<>'' and trueCV=''");
    }

    public Game.GameCharacter get(String id) {
        return get(creator, "select id,`name`,cv from `character` where id=?", id);
    }

    public static class GameCharCreator implements ObjectCreator<Game.GameCharacter> {
        @Override
        public Game.GameCharacter create(ResultSet resultSet) throws SQLException {

            var g = new Game.GameCharacter();

            g.name = resultSet.getString("name");
            g.cv = resultSet.getString("cv");
//                    g.intro = resultSet.getString("intro");
//                    g.gameId = resultSet.getInt("gameId");
//                    g.trueCV = resultSet.getString("trueCV");
            return g;
        }
    }
}
