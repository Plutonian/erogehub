package com.goexp.galgame.gui.view.task;

import com.goexp.galgame.gui.db.mongo.query.GameQuery;
import com.goexp.galgame.gui.model.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;

public class GameCharListTask extends Task<ObservableList<Game.GameCharacter>> {

    private int gameId;

    private GameQuery.GameCharQuery gameCharQuery = new GameQuery.GameCharQuery();

    public GameCharListTask(int gameId) {
        this.gameId = gameId;

    }

    @Override
    protected ObservableList<Game.GameCharacter> call() {


        var g = GameQuery.GameCharQuery.tlp.query()
                .where(eq(gameId))
                .select(include("gamechar"))
                .one();

        return FXCollections.observableArrayList(g.gameCharacters);
    }
}
