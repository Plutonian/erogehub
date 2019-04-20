package com.goexp.galgame.gui.task;

import com.goexp.galgame.gui.db.mongo.Query;
import com.goexp.galgame.gui.model.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class GameCharListTask extends Task<ObservableList<Game.GameCharacter>> {

    private int gameId;

    public GameCharListTask(int gameId) {
        this.gameId = gameId;

    }

    @Override
    protected ObservableList<Game.GameCharacter> call() {


        var g = Query.GameQuery.GameCharQuery.tlp.query()
                .where(eq(gameId))
                .one();

        return Optional.ofNullable(g.gameCharacters)
                .map(FXCollections::observableArrayList)
                .orElse(FXCollections.emptyObservableList());
    }
}
