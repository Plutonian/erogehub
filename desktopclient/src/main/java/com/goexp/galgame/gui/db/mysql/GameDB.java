package com.goexp.galgame.gui.db.mysql;

import com.goexp.common.db.mysql.DBOperatorTemplate;
import com.goexp.galgame.gui.model.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameDB extends DBOperatorTemplate {

    public void update(Game game) {
        try {

            runner.update("update game_copy1 set `name`=?,brand=?,publishDate=? where id=?"
                    , game.name
                    , game.brand.id
                    , game.publishDate.toString()
                    , game.id
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateTag(Game game) {
        try {

            runner.update("update game_copy1 set `tag`=? where id=?"
                    , game.tag
                    , game.id
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void update2(Game game) {
        try {

            runner.update("update game_copy1 set `painter`=?,writer=?,`type`=?,tag=?,story=? where id=?"
                    , game.painter
                    , game.writer
                    , game.type
                    , game.tag
                    , game.story
                    , game.id
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBrand(Game game) {
        try {

            runner.update("update game_copy1 set brand=? where id=?"
                    , game.brand.id
                    , game.id
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTitle(Game game) {
        try {

            runner.update("update game_copy1 set `name`=? where id=?"
                    , game.name
                    , game.id
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateIntro(List<Game> games) {
        try {

            var param = new Object[games.size()][];

            for (var i = 0; i < games.size(); i++) {
                var g = games.get(i);
                param[i] = new Object[]{g.story, g.id};
            }

            runner.batch("update game_copy1 set `story`=? where id=?", param);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean exist(int id) {
        try {

            return runner.query("select 1 from game_copy1 where id=? limit 1", ResultSet::next, id);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static class CharDB extends DBOperatorTemplate {


        public void updateIntro(List<Game.GameCharacter> games) {
            try {

                var param = new Object[games.size()][];

                for (var i = 0; i < games.size(); i++) {
                    var g = games.get(i);
                    param[i] = new Object[]{g.intro, g.id};
                }


                runner.batch("update `character` set intro=? where id=?", param);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void update2(Game.GameCharacter game) {
            try {

                runner.update("update `character` set `name`=?,cv=? where id=?"
                        , game.name
                        , game.cv
                        , game.id
                );


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void updateImg(Game.GameCharacter game) {
            try {

                runner.update("update `character` set `img`=? where id=?"
                        , game.img
                        , game.id
                );


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void updateTitle(Game.GameCharacter game) {
            try {

                runner.update("update `character` set `name`=? where id=?"
                        , game.name
                        , game.id
                );


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void updateTrueCV(Game.GameCharacter game) {
            try {

                runner.update("update `character` set `trueCV`=? where id=?"
                        , game.trueCV
                        , game.id
                );


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static class StarDB extends DBOperatorTemplate {


        public void update(Game game) {
            try {
                runner.update("update game_star set `star`=? where id=?", game.star, game.id);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void batchUpdate(List<Game> games) {
            try {


                var param = new Object[games.size()][];

                for (var i = 0; i < games.size(); i++) {
                    var img = games.get(i);
                    param[i] = new Object[]{img.star, img.id};
                }
                runner.batch("update game_star set `star`=? where id=?", param);


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static class StateDB extends DBOperatorTemplate {


        public void update(Game game) {
            try {
                runner.update("update game_state set `state`=? where id=?", game.state.get().getValue(), game.id);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void batchUpdate(List<Game> games) {
            try {


                var param = new Object[games.size()][];

                for (var i = 0; i < games.size(); i++) {
                    var game = games.get(i);
                    param[i] = new Object[]{game.state.get().getValue(), game.id};
                }
                runner.batch("update game_state set `state`=? where id=?", param);


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
