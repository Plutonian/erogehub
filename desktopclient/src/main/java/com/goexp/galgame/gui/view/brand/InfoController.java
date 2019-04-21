package com.goexp.galgame.gui.view.brand;

import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.game.GameSearchTask;
import com.goexp.galgame.gui.util.DefaultController;
import com.goexp.galgame.gui.util.TaskService;
import com.goexp.galgame.gui.view.game.listview.DataViewController;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Service;
import javafx.fxml.FXML;

public class InfoController extends DefaultController {

    /**
     * UI Com
     */

    @FXML
    public TitlePartController titleController;

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


    private Service<ObservableList<Game>> gameByBrand = new TaskService<>(() -> new GameSearchTask.ByBrand(brand.id));


    /**
     * Event
     */


    protected void initialize() {


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
