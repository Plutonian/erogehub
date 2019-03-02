package com.goexp.galgame.data.db.importor.mysql;

import com.goexp.common.db.mysql.DBOperatorTemplate;
import com.goexp.galgame.data.model.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameDB extends DBOperatorTemplate {


    public void insert(Game game) {
        try {

            runner.update("insert into game_copy1 (id,`name`,brandid,publishdate,smallimg)values (?,?,?,?,?)",
                    game.id
                    , game.name
                    , game.brandId
                    , game.publishDate.toString()
                    , game.smallImg
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Game game) {
        try {

            runner.update("update game_copy1 set `publishDate`=?,smallimg=? where id=?"
                    , game.publishDate.toString()
                    , game.smallImg
                    , game.id
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void updateDate(Game game) {
//        try {
//
//            runner.update("update game_copy1 set `publishDate`=? where id=?"
//                    , game.publishDate.toString()
//                    , game.id
//            );
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void updateTag(Game game) {
//        try {
//
//            runner.update("update game_copy1 set `tag`=? where id=?"
//                    , game.tag
//                    , game.id
//            );
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void batchUpdateAll(List<Game> games) {
//        try {
//
//            var param = new Object[games.size()][];
//
//            for (var i = 0; i < games.size(); i++) {
//                var game = games.get(i);
//                param[i] = new Object[]{game.painter, game.writer, game.type, game.tag, game.story, game.id};
//            }
//
//            runner.batch("update game_copy1 set `painter`=?,writer=?,`type`=?,tag=?,story=? where id=?", param);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void updateAll(Game game) {
        try {

            runner.update("update game_copy1 set `painter`=?,writer=?,`type`=?,tag=?,story=?,intro=?,brandId=? where id=?"
                    , game.painter
                    , game.writer
                    , game.type
                    , game.tag
                    , game.story
                    , game.intro
                    , game.brandId
                    , game.id
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void updateBrand(Game game) {
//        try {
//
//            runner.update("update game_copy1 set brandId=? where id=?"
//                    , game.brandId
//                    , game.id
//            );
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void updateSmallImg(Game game) {
//        try {
//
//            runner.update("update game_copy1 set `smallimg`=? where id=?"
//                    , game.imgUrl
//                    , game.id
//            );
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateTitle(Game game) {
//        try {
//
//            runner.update("update game_copy1 set `name`=? where id=?"
//                    , game.name
//                    , game.id
//            );
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateStory(Game game) {
//        try {
//
//            runner.update("update game_copy1 set `story`=? where id=?"
//                    , game.story
//                    , game.id
//            );
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void updateIntro(Game game) {
//        try {
//
//            runner.update("update game_copy1 set `intro`=? where id=?"
//                    , game.intro
//                    , game.id
//            );
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateStory(List<Game> games) {
//        try {
//
//            var param = new Object[games.size()][];
//
//            for (var i = 0; i < games.size(); i++) {
//                var g = games.get(i);
//                param[i] = new Object[]{g.story, g.id};
//            }
//
//            runner.batch("update game_copy1 set `story`=? where id=?", param);
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


    public boolean exist(int id) {
        try {

            return runner.query("select 1 from game_copy1 where id=? limit 1", ResultSet::next, id);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static class Star {
        public void insert(Game game) {
            try {
                runner.update("insert into game_star (id) values (?)", game.id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static class State {
        public void insert(Game game) {
            try {
                runner.update("insert into game_state (id) values (?)", game.id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
