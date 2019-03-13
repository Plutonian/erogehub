package com.goexp.galgame.gui.view.search.frombrand.brand;

import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.dataview.DataViewController;
import com.goexp.galgame.gui.view.search.frombrand.brand.task.BrandGetGameTask;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrandInfoController {

    private final Logger logger = LoggerFactory.getLogger(BrandInfoController.class);

    /**
     * UI Com
     */

    @FXML
    public BrandTitleController titleController;

    @FXML
    public DataViewController dataViewController;

    private FilteredList<Game> filteredGames;


    /***
     * Biz
     */

    private Brand brand = new Brand();

    /**
     * Service
     */


    private Service<ObservableList<Game>> gameByBrand = new Service<>() {
        @Override
        protected Task<ObservableList<Game>> createTask() {
            return new BrandGetGameTask.ByBrand(brand.id);
        }
    };


    /**
     * Event
     */

    @FXML
    private void initialize() {


        ChangeListener<Throwable> exceptionHandler = (observable, oldValue, newValue) -> {
            if (newValue != null)
                newValue.printStackTrace();
        };

        ChangeListener<ObservableList<Game>> searchHandler = (observable, oldValue, newValue) -> {

            if (newValue != null) {
                load(newValue);
            }

        };

        gameByBrand.valueProperty().addListener(searchHandler);
        gameByBrand.exceptionProperty().addListener(exceptionHandler);

        dataViewController.progessloading.visibleProperty().bind(gameByBrand.runningProperty());
        dataViewController.reloadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {
                gameByBrand.restart();
            }
        });

    }


    private void load(ObservableList<Game> games) {
        filteredGames = new FilteredList<>(games);
        dataViewController.setItems(filteredGames);
    }

    public void load(Brand brand) {

        logger.info("<Brand> {}", brand);
        this.brand = brand;

        titleController.init(brand);
        gameByBrand.restart();
    }

}
