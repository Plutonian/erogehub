package com.goexp.galgame.gui.util;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.dataview.DataViewController;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;

import java.io.IOException;

public class CommonTabController {

    public Region node;
    public DataViewController controller;

    private Service<ObservableList<Game>> gameSearchService;


    public CommonTabController(Service<ObservableList<Game>> service) {
        this.gameSearchService = service;

        init();
    }

    private void init() {

        var dataView = new DateView();
        dataView.invoke();
        node = dataView.node;

        controller = dataView.controller;

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

    private class DateView {
        public DataViewController controller;

        public Region node;

        DateView() {
        }

        void invoke() {
            var loader = new FXMLLoader(getClass().getClassLoader().getResource("view/game_explorer/game_dataview.fxml"));
            try {
                node = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller = loader.getController();
        }
    }


}
