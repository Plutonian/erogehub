package com.goexp.galgame.gui.util;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.game.dataview.DataViewController;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.scene.layout.Region;

public class CommonTabController {

    public Region node;
    public DataViewController controller;

    private Service<ObservableList<Game>> gameSearchService;


    public CommonTabController(Service<ObservableList<Game>> service) {
        this.gameSearchService = service;

        init();
    }

    private void init() {

        final var loader = new FXMLLoaderProxy<Region, DataViewController>("view/game_explorer/game_dataview.fxml");
        node = loader.node;
        controller = loader.controller;

        controller.reloadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue)
                gameSearchService.restart();
        });

        gameSearchService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                controller.setItems(newValue);
            }
        });

        controller.progessloading.visibleProperty().bind(gameSearchService.runningProperty());

        ChangeListener<Throwable> exceptionHandler = (observable, oldValue, newValue) -> {
            if (newValue != null)
                newValue.printStackTrace();
        };
        gameSearchService.exceptionProperty().addListener(exceptionHandler);
    }

    public void load() {
        gameSearchService.restart();
    }

}
