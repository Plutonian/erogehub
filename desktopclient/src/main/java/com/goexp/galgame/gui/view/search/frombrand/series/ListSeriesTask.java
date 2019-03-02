package com.goexp.galgame.gui.view.search.frombrand.series;

import com.goexp.galgame.gui.db.mysql.query.SeriesQuery;
import com.goexp.galgame.gui.model.Series;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.stream.Collectors;

public class ListSeriesTask extends Task<ObservableList<Series>> {


    private SeriesQuery seriesQuery = new SeriesQuery();

    private int brandid;


    public ListSeriesTask(int brandid) {
        this.brandid = brandid;
    }

    @Override
    protected ObservableList<Series> call() throws Exception {


        var list = seriesQuery.list(brandid).stream()
                .peek(series -> {
                    seriesQuery.fill(series);
                })
                .collect(Collectors.toList());

//        if (games != null && games.size() > 0)
//            seriesDB.addGame(id, games);


        return FXCollections.observableArrayList(list);
    }
}
