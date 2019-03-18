package com.goexp.galgame.gui.db.mongo;

import com.goexp.common.db.mongo.DBOperatorTemplate;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class GameDB {

    private static DBOperatorTemplate tlp = new DBOperatorTemplate("galgame", "game");

    public static class StarDB {

        private final Logger logger = LoggerFactory.getLogger(StarDB.class);


        public void update(Game game) {

            logger.debug("<update> {}", game);

            tlp.exec(documentMongoCollection -> {
                documentMongoCollection.updateOne(eq(game.id), set("star", game.star));
            });
        }

//        public void batchUpdate(List<Game> games) {
//            try {
//
//
//                var param = new Object[games.size()][];
//
//                for (var i = 0; i < games.size(); i++) {
//                    var img = games.get(i);
//                    param[i] = new Object[]{img.star, img.id};
//                }
//                runner.batch("update game_star set `star`=? where id=?", param);
//
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

    }

    public static class StateDB {


        public void update(Game game) {
            tlp.exec(documentMongoCollection -> {
                documentMongoCollection.updateOne(
                        eq(game.id)
                        , set("state", game.state.get().getValue())
                );
            });
        }

        public void update(int brandId) {
            tlp.exec(documentMongoCollection -> {
                documentMongoCollection.updateMany(
                        eq("brandId", brandId)
                        , set("state", GameState.BLOCK.getValue())
                );
            });
        }


        public void batchUpdate(List<Game> games) {


            tlp.exec(documentMongoCollection -> {

                games.forEach(game -> {
                    documentMongoCollection.updateOne(
                            eq(game.id)
                            , set("state", game.state.get().getValue())
                    );
                });

            });

        }
    }
}
