package com.goexp.galgame.gui.view.game;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.game.listview.DataViewController;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CommonTabController {

    public Region node;
    public DataViewController controller;

    private Service<ObservableList<Game>> gameSearchService;
    private URL resource = DataViewController.class.getResource("dataview.fxml");


    public CommonTabController(Supplier<Task<ObservableList<Game>>> task) {
        this.gameSearchService = new TaskService<>(task);

        init();
    }

    public CommonTabController(Service<ObservableList<Game>> service) {
        this.gameSearchService = service;

        init();
    }

    private void init() {

        final var loader = new FXMLLoaderProxy<Region, DataViewController>(resource);
        node = loader.node;
        controller = loader.controller;

        controller.reloadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue)
                gameSearchService.restart();
        });

        gameSearchService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                if (initPredicate != null)
                    controller.load(newValue, initPredicate);
                else
                    controller.load(newValue);
            }
        });

        controller.progessloading.visibleProperty().bind(gameSearchService.runningProperty());

    }


    private Predicate<Game> initPredicate;

    public void load(Predicate<Game> initPredicate) {

        this.initPredicate = initPredicate;
        load();
    }

    public void load() {
        gameSearchService.restart();
    }

}
