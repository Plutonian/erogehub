package com.goexp.galgame.data.db.importor.mysql;

import com.goexp.common.db.mysql.DBUpdateTemplate;
import com.goexp.galgame.data.model.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameCharDB extends DBUpdateTemplate {


    public void insert(Game.GameCharacter game) {
        try {

            runner.update("insert into `character` ()values (?,?,?,?,?,default,?,?)",
                    game.id
                    , game.name
                    , game.cv
                    , game.intro
                    , game.gameId
//                    , game.trueCV
                    , game.img
                    , game.index
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    public void batchInsert(List<Game.GameCharacter> list) {
//        try {
//            var param = new Object[list.size()][];
//
//            for (var i = 0; i < list.size(); i++) {
//                var gameCharacter = list.get(i);
//                param[i] = new Object[]{gameCharacter.id,
//                        gameCharacter.name,
//                        gameCharacter.cv,
//                        gameCharacter.intro,
//                        gameCharacter.gameId,
////                        gameCharacter.trueCV,
//                        gameCharacter.img,
//                        gameCharacter.index,
//                };
//
//            }
//
//            runner.batch("insert into `character` ()values (?,?,?,?,?,default,?,?)", param);
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    public void updateIntro(List<Game.GameCharacter> games) {
//        try {
//
//            var param = new Object[games.size()][];
//
//            for (var i = 0; i < games.size(); i++) {
//                var g = games.get(i);
//                param[i] = new Object[]{g.intro, g.id};
//            }
//
//
//            runner.batch("update `character` set intro=? where id=?", param);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void updateALL(Game.GameCharacter game) {
        try {

            runner.update("update `character` set `name`=?,cv=?,`intro`=?,`img`=? where id=?"
                    , game.name
                    , game.cv
                    , game.intro
                    , game.img
                    , game.id
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateALLWithoutCV(Game.GameCharacter game) {
        try {

            runner.update("update `character` set `name`=?,`intro`=?,`img`=? where id=?"
                    , game.name
                    , game.intro
                    , game.img
                    , game.id
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void updateImg(Game.GameCharacter game) {
//        try {
//
//            runner.update("update `character` set `img`=? where id=?"
//                    , game.img
//                    , game.id
//            );
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    public void updateTitle(Game.GameCharacter game) {
//        try {
//
//            runner.update("update `character` set `name`=? where id=?"
//                    , game.name
//                    , game.id
//            );
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

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


    public boolean exist(String id) {
        try {

            return runner.query("select 1 from `character` where id=? limit 1", ResultSet::next, id);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


}
