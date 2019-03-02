package com.goexp.galgame.gui.view.dataview.imglistview;

import com.goexp.galgame.gui.view.dataview.imglistview.cell.GameImgListCell;
import com.goexp.galgame.gui.model.Game;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.util.stream.Collectors;

public class GameImgListController {

    final private int pageSize = 5;
    @FXML
    private FlowPane imgList;
    @FXML
    private ScrollPane rootPanel;
    private ObservableList<Game> cacheList;
    private int index = 1;
    private int page = 0;

    @FXML
    private void initialize() {

        rootPanel.vvalueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null && newValue.doubleValue() > 0.9 * rootPanel.getVmax()) {
                loadItem();
            }
        });
    }

    public void load(ObservableList<Game> item) {

        cacheList = item;
        index = 1;
        page = (int) Math.ceil(cacheList.size() / (double) pageSize);
        rootPanel.setVvalue(rootPanel.getVmin());

        imgList.getChildren().clear();

        loadItem();

    }


    private void loadItem() {

        if (index <= page) {
            var nodes = cacheList.stream()
                    .skip((index - 1) * pageSize)
                    .limit(pageSize)
                    .map(game -> {
                        return new GameImgListCell(game).invoke();
                    })
                    .collect(Collectors.toList());

            index++;

            imgList.getChildren().addAll(nodes);
        }

    }


}
