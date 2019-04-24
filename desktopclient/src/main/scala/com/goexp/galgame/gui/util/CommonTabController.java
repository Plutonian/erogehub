package com.goexp.galgame.gui.util;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.game.listview.DataViewController;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;

import java.util.function.Supplier;

public class CommonTabController {

    public Region node;
    public DataViewController controller;

    private Service<ObservableList<Game>> gameSearchService;


    public CommonTabController(Supplier<Task<ObservableList<Game>>> task) {
        this.gameSearchService = new TaskService<>(task);

        init();
    }

    public CommonTabController(Service<ObservableList<Game>> service) {
        this.gameSearchService = service;

        init();
    }

    private void init() {

        final var loader = new FXMLLoaderProxy<Region, DataViewController>("view/game_explorer/dataview.fxml");
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

    }

    public void load() {
        gameSearchService.restart();
    }

}
