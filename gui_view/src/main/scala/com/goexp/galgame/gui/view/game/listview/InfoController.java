package com.goexp.galgame.gui.view.game.listview;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.util.Comparator;
import java.util.stream.Collectors;

public class InfoController extends DefaultController {

    @FXML
    private PieChart pieChartTags;


    public void load(ObservableList<Game> games) {
        makeChart(games);
    }


    private void makeChart(ObservableList<Game> newValue) {

        var data = newValue.stream()
                .filter(g -> g.tag.size() > 0)
                .flatMap(g -> g.tag.stream())
                .collect(Collectors.groupingBy(str -> str))
                .entrySet().stream()
                .sorted(Comparator.comparing(v -> v.getValue().size(), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // top 10

        var PieData = data.stream().limit(10)
                .map(k -> {

                    var key = k.getKey();
                    var value = k.getValue().size();

                    logger.debug("<makeChart> Name:{},Value:{}", key, value);
                    return new PieChart.Data(key, value);
                })
                .collect(Collectors.toList());


        pieChartTags.setData(FXCollections.observableArrayList(PieData));

    }

    @Override
    protected void initialize() {

    }
}
