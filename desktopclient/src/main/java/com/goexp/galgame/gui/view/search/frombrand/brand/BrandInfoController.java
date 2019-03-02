package com.goexp.galgame.gui.view.search.frombrand.brand;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.view.dataview.DataViewController;
import com.goexp.galgame.gui.util.Images;
import com.goexp.galgame.gui.view.search.frombrand.brand.task.BrandGetGameTask;
import com.goexp.galgame.gui.view.search.frombrand.series.AddSeriesTask;
import com.goexp.galgame.gui.view.search.frombrand.series.ListSeriesTask;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.model.Series;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BrandInfoController {

    private final Logger logger = LoggerFactory.getLogger(BrandInfoController.class);

    /**
     * UI Com
     */

    @FXML
    public BrandTitleController titleController;

    @FXML
    public DataViewController dataViewController;

    @FXML
    private TextField txtFieldName;

    @FXML
    private TitledPane seriesPanel;

    @FXML
    private TitledPane createSeriesPanel;

    @FXML
    private Button btnCreateSeries;

    @FXML
    private ListView<Series> listSeries;

    @FXML
    private Accordion placehoder;

    private ObservableList<Game> games;

    private FilteredList<Game> filteredGames;

    private String seriesName;

    private GameState gameState = GameState.HOPE;

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

    private Service<ObservableList<Series>> seriesByBrand = new Service<>() {
        @Override
        protected Task<ObservableList<Series>> createTask() {
            return new ListSeriesTask(brand.id);
        }
    };

    private Service<Boolean> createSeries = new Service<>() {
        @Override
        protected Task<Boolean> createTask() {
            return new AddSeriesTask(games, brand.id, seriesName);
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


        seriesByBrand.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {

                if (newValue.size() > 0) {
                    var s = new Series();
                    s.brandId = -1;
                    s.id = "-1";
                    s.name = "ALL";
                    newValue.add(s);
                }

                listSeries.setItems(newValue);
            }

        });

        listSeries.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Series> call(ListView<Series> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Series item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty) {

                            if (item.brandId != -1) {

                                var box = new VBox();

                                if (item.games != null && item.games.size() > 0) {
                                    var imgView = new ImageView(Images.GameImage.tiny(item.games.get(0), item.games.get(0).smallImg));
                                    box.getChildren().add(imgView);
                                }

                                var lb = new Label(item.name);
                                lb.setStyle("-fx-font-size:14px");
                                box.getChildren().add(lb);

                                setGraphic(box);

                            } else {
                                setGraphic(new Label(item.name));
                            }
                        } else {
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });
        listSeries.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {

                if (newValue.brandId != -1) {
                    filteredGames.setPredicate(game -> newValue.games.contains(game));

                    seriesPanel.setText("Series:" + newValue.name);
                } else {
                    filteredGames.setPredicate(null);
                }

            }
        });


        createSeries.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue)
                seriesByBrand.restart();
        });

        gameByBrand.valueProperty().addListener(searchHandler);
        gameByBrand.exceptionProperty().addListener(exceptionHandler);

        dataViewController.progessloading.visibleProperty().bind(gameByBrand.runningProperty());
        dataViewController.tableViewController.table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtFieldName.setText(newValue.name.split("[\\s〜\\-【]")[0]);
            }
        });
        dataViewController.reloadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {
                seriesByBrand.restart();
                gameByBrand.restart();
            }
        });

        btnCreateSeries.disableProperty().bind(txtFieldName.textProperty().isEmpty());

        var panels = placehoder.getPanes();
        dataViewController.groupPanel.getPanes().addAll(panels);
        panels.clear();
    }


    @FXML
    private void createSeries_onAction(ActionEvent actionEvent) {

        seriesName = txtFieldName.getText().trim();

        var find = listSeries.getItems().stream().anyMatch(series -> series.name.equals(seriesName));


        var msgBox = new Alert(Alert.AlertType.ERROR);
        msgBox.setTitle("Error");


        if (find) {
            msgBox.setContentText("Allrelly exist!!!");
            msgBox.show();
        } else {

            games = dataViewController.tableViewController.table.getSelectionModel().getSelectedItems();

            createSeries.restart();
        }

    }

    @FXML
    private void swtichSeriesPanel_onAction(ActionEvent actionEvent) {

        createSeriesPanel.setVisible(!createSeriesPanel.isVisible());
    }

    private void load(ObservableList<Game> games) {
        filteredGames = new FilteredList<>(games);
        dataViewController.setItems(filteredGames);
    }

    public void load(Brand brand) {
        this.brand = brand;

        titleController.init(brand);
        seriesByBrand.restart();
        gameByBrand.restart();
    }

}
