package com.goexp.galgame.gui.view.search.frombrand.series;

import com.goexp.galgame.gui.db.mysql.SeriesDB;
import com.goexp.galgame.gui.model.Game;
import javafx.concurrent.Task;

import java.util.List;

public class AddSeriesTask extends Task<Boolean> {

    private SeriesDB seriesDB = new SeriesDB();


    private List<Game> games;

    private int brandid;

    private String name;


    public AddSeriesTask(List<Game> games, int brandid, String name) {
        this.games = games;
        this.brandid = brandid;
        this.name = name;
    }

    @Override
    protected Boolean call() throws Exception {

        var id = String.format("%d_%s", brandid, name);

        if (!seriesDB.exist(id)) {
            seriesDB.insert(id, brandid, name);
        }


        if (games != null && games.size() > 0)
            seriesDB.addGame(id, games);


        return true;
    }
}
