package com.goexp.galgame.data.db.importor.mysql;

import com.goexp.common.db.mysql.DBUpdateTemplate;
import com.goexp.galgame.data.model.Game;

import java.sql.SQLException;

public class GameCharDB extends DBUpdateTemplate {


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
