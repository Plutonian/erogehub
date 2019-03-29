package com.goexp.galgame.gui.view.task;

import com.goexp.galgame.gui.db.mongo.query.GameQuery;
import com.goexp.galgame.gui.model.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class GameImgListTask extends Task<ObservableList<Game.GameImg>> {

    private int gameId;

    public GameImgListTask(int gameId) {
        this.gameId = gameId;

    }

    @Override
    protected ObservableList<Game.GameImg> call() {

        new GameQuery.GameImgQuery();

        var g = GameQuery.GameImgQuery.tlp.query()
                .where(eq(gameId))
                .one();
        return Optional.ofNullable(g.gameImgs)
                .map(FXCollections::observableArrayList)
                .orElse(FXCollections.emptyObservableList());
    }
}
