package com.goexp.galgame.gui.view.task;

import com.goexp.galgame.gui.db.mongo.query.GameQuery;
import com.goexp.galgame.gui.model.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class GameImgListTask extends Task<ObservableList<Game.GameImg>> {

    private int gameId;

    public GameImgListTask(int gameId) {
        this.gameId = gameId;

    }

    @Override
    protected ObservableList<Game.GameImg> call() {

        return FXCollections.observableArrayList(new GameQuery.GameImgQuery().list(gameId));
    }
}
