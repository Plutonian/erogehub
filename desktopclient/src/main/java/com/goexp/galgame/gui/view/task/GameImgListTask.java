package com.goexp.galgame.gui.view.task;

import com.goexp.galgame.gui.db.mongo.query.GameQuery;
import com.goexp.galgame.gui.model.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;

public class GameImgListTask extends Task<ObservableList<Game.GameImg>> {

    private int gameId;

    public GameImgListTask(int gameId) {
        this.gameId = gameId;

    }

    @Override
    protected ObservableList<Game.GameImg> call() {

        new GameQuery.GameImgQuery();

        var g = GameQuery.GameImgQuery.tlp.query()
                .where(eq("_id", gameId))
                .select(include("simpleImg"))
                .one();
        return FXCollections.observableArrayList(g.gameImgs);
    }
}
