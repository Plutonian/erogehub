package com.goexp.galgame.gui.view.game.listview;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.PanelTask;
import com.goexp.galgame.gui.util.DefaultController;
import com.goexp.galgame.gui.util.TaskService;
import com.goexp.galgame.gui.view.game.listview.sidebar.FilterPanelController;
import com.goexp.galgame.gui.view.game.listview.tableview.TableController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Predicate;

public class DataViewController extends DefaultController {

    public BooleanProperty reloadProperty = new SimpleBooleanProperty(false);

    @FXML
    public TableController tableViewController;

    @FXML
    public ProgressBar progessloading;
    @FXML
    public Accordion groupPanel;
    @FXML
    private FilterPanelController filterPanelController;
    @FXML
    private FilterPanelController.BrandGroupController brandGroupController;
    @FXML
    private FilterPanelController.DateGroupController dateGroupController;
    @FXML
    private Label lbItemCount;
    @FXML
    private ToggleGroup gameViewChange;
    @FXML
    private ToggleButton toggList;
    @FXML
    private ToggleButton toggGrid;
    @FXML
    private ToggleButton toggImg;
    @FXML
    private TableView<Game> tableView;
    @FXML
    private ListView<Game> listSimple;
    @FXML
    private Region imgView;
    @FXML
    private Region filterPanel;
    @FXML
    private Button btnHide;
    @FXML
    private VBox tagFlow;

    private FilteredList<Game> filteredGames;

    private Predicate<Game> groupPredicate;

    private Service<List<HBox>> groupTagServ = new TaskService<>(() -> new PanelTask.GroupTag(filteredGames));

//    private ObservableList<Game> cacheGames;


    protected void initialize() {
        initSwitchBar();
        initSideBar();
        initTagPanel();
    }

    private void initTagPanel() {
        groupTagServ.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tagFlow.getChildren().setAll(newValue);
            }
        });
    }

    private void initSwitchBar() {
        toggGrid.setUserData(tableView);
        toggList.setUserData(listSimple);
        toggImg.setUserData(imgView);

        gameViewChange.selectedToggleProperty().addListener((observable, oldValue, checkedBox) -> {
            if (checkedBox != null) {
                ((Region) checkedBox.getUserData()).toFront();
            } else {
                gameViewChange.selectToggle(oldValue);
            }

        });
    }

    private void initSideBar() {
        btnHide.setUserData(true);
        btnHide.getProperties().put("groupPanel", groupPanel.getPrefWidth());
        btnHide.setOnAction(event -> {
            var state = (Boolean) btnHide.getUserData();
            var width = (double) btnHide.getProperties().get("groupPanel");

            state = !state;

            groupPanel.setVisible(state);
            groupPanel.setPrefWidth(state ? width : 0);
            btnHide.setUserData(state);

        });


        // #2
        groupPanel.setExpandedPane(groupPanel.getPanes().get(1));

        initSidebarContentView();
    }

    private void initSidebarContentView() {
        filterPanelController.onSetProperty.addListener((o, old, newV) -> {
            if (newV != null && newV) {
                var filterPredicate = filterPanelController.predicate;
                var p = groupPredicate != null ? filterPredicate.and(groupPredicate) : filterPredicate;
                filteredGames.setPredicate(p);
            }
        });

        brandGroupController.onSetProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                groupPredicate = brandGroupController.predicate;

                var filterPredicate = filterPanelController.predicate;
                var p = filterPredicate != null ? groupPredicate.and(filterPredicate) : groupPredicate;
                filteredGames.setPredicate(p);
            }
        });
        dateGroupController.onSetProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                groupPredicate = dateGroupController.predicate;

                var filterPredicate = filterPanelController.predicate;
                var p = filterPredicate != null ? groupPredicate.and(filterPredicate) : groupPredicate;
                filteredGames.setPredicate(p);
            }
        });
    }



    public void setItems(ObservableList<Game> games) {
        filterPanel.setVisible(true);
//        this.cacheGames = games;

        groupPredicate = null;

        filteredGames = new FilteredList<>(games);
        filteredGames.predicateProperty().addListener((o, old, newV) -> {
            if (newV != null) {
                tableView.scrollTo(0);
                listSimple.scrollTo(0);
                resetCount(filteredGames);
            }
        });

        // set filter
        filteredGames.setPredicate(g ->
                g.state.get().getValue() > GameState.BLOCK.getValue() && !(g.star > 0 && g.star < 3)
        );

        var sortedData = new SortedList<>(filteredGames);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        loadItems(sortedData);

        setSideBarData(filteredGames);
    }

    private void setSideBarData(FilteredList<Game> filteredGames) {
        dateGroupController.init(filteredGames);
        brandGroupController.init(filteredGames);

        groupTagServ.restart();
    }

    private void loadItems(SortedList<Game> sortedData) {

        tableView.setItems(sortedData);
        tableView.scrollTo(0);

        listSimple.setItems(sortedData);
        listSimple.scrollTo(0);

    }

    private void loadItems(ObservableList<Game> sortedData) {
        resetCount(sortedData);
        tableView.setItems(sortedData);
        tableView.scrollTo(0);

        listSimple.setItems(sortedData);
        listSimple.scrollTo(0);
    }

    private void resetCount(List<Game> filteredGames) {
        lbItemCount.setText(String.format("%d 件", filteredGames.size()));
    }


    @FXML
    private void reload_OnAction(ActionEvent event) {

        reloadProperty.set(true);
        reloadProperty.set(false);

    }
}
