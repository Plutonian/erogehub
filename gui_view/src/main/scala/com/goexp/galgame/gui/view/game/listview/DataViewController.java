package com.goexp.galgame.gui.view.game.listview;

import com.goexp.galgame.common.model.game.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.game.panel.group.ByCV;
import com.goexp.galgame.gui.task.game.panel.group.ByTag;
import com.goexp.galgame.gui.task.game.panel.group.node.DefaultItem;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.game.listview.imglist.ImgListViewController;
import com.goexp.galgame.gui.view.game.listview.sidebar.BrandGroupController;
import com.goexp.galgame.gui.view.game.listview.sidebar.DateGroupController;
import com.goexp.galgame.gui.view.game.listview.sidebar.FilterPanelController;
import com.goexp.galgame.gui.view.game.listview.tableview.TableController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.List;
import java.util.function.Predicate;

public class DataViewController extends DefaultController {

    public final BooleanProperty reloadProperty = new SimpleBooleanProperty(false);


    /**
     * Controllers
     */
    @FXML
    private ImgListViewController imgViewController;

    @FXML
    public TableController tableViewController;
    @FXML
    private FilterPanelController filterPanelController;
    @FXML
    private BrandGroupController brandGroupController;
    @FXML
    private DateGroupController dateGroupController;


    /**
     * Status bar
     */
    @FXML
    private Label lbItemCount;
    @FXML
    public ProgressBar progessloading;


    /**
     * Toggle
     */
    @FXML
    private ToggleGroup gameViewChange;
    //    @FXML
//    private ToggleButton toggList;
    @FXML
    private ToggleButton toggGrid;
    @FXML
    private ToggleButton toggSmall;
    @FXML
    private ToggleButton toggImg;


    /**
     * main panel
     */
    @FXML
    private TableView<Game> tableView;
    @FXML
    private ListView<Game> smallListSimple;
    //    @FXML
//    private ListView<Game> smallListSimple;
    @FXML
    private Region imgView;


    /**
     * Sidebar
     */
    @FXML
    private Region groupPanel;

    @FXML
    private Region filterPanel;
    @FXML
    private Button btnHide;
    @FXML
    private ListView<DefaultItem> cvList;
    @FXML
    private ListView<DefaultItem> tagList;


    private FilteredList<Game> filteredGames;

    private Predicate<Game> groupPredicate;

    private final Service<List<DefaultItem>> groupCVServ = new TaskService<>(() -> new ByCV(filteredGames));
    private final Service<List<DefaultItem>> groupTagServ = new TaskService<>(() -> new ByTag(filteredGames));


    protected void initialize() {
        initSwitchBar();
        initSideBar();
        initGroupPanel();
        btnHide.fire();
    }

    private void initGroupPanel() {
        cvList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(DefaultItem cvItemNode, boolean empty) {
                super.updateItem(cvItemNode, empty);
                setText(null);
                setGraphic(null);

                if (cvItemNode != null && !empty) {
                    setGraphic(new HBox(Tags.toNodes(cvItemNode.title), new Label(String.valueOf(cvItemNode.count))));
                }

            }
        });

        tagList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(DefaultItem tagNode, boolean empty) {
                super.updateItem(tagNode, empty);
                setText(null);
                setGraphic(null);

                if (tagNode != null && !empty) {
                    setGraphic(new HBox(Tags.toNodes(tagNode.title), new Label(String.valueOf(tagNode.count))));
                }

            }
        });


        groupCVServ.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cvList.setItems(FXCollections.observableList(newValue));
            }
        });
        groupTagServ.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tagList.setItems(FXCollections.observableList(newValue));
            }
        });
    }

    private void initSwitchBar() {
        toggGrid.setUserData(tableView);
        toggSmall.setUserData(smallListSimple);
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


        initSidebarContentView();
    }

    private void initSidebarContentView() {
        filterPanelController.onSetProperty.addListener((o, old, newV) -> {
            if (newV != null && newV) {
                var load = groupPredicate == null;

                var filterPredicate = filterPanelController.predicate;
                var p = groupPredicate != null ? filterPredicate.and(groupPredicate) : filterPredicate;
                filteredGames.setPredicate(p);

                if (load)
                    setSideBarData(filteredGames);
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


    public void load(ObservableList<Game> games, Predicate<Game> initPredicate) {

        filterPanel.setVisible(true);

        groupPredicate = null;

        filteredGames = new FilteredList<>(games);
        filteredGames.predicateProperty().addListener((o, old, newV) -> {
            if (newV != null) {
                tableView.scrollTo(0);
                smallListSimple.scrollTo(0);
                resetCount(filteredGames);
            }
        });

        // set filter
        filteredGames.setPredicate(initPredicate);

        // set defaultPredicate
        filterPanelController.predicate = initPredicate;

        var sortedData = new SortedList<>(filteredGames);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        loadItems(sortedData);

        setSideBarData(filteredGames);
    }


    public void load(ObservableList<Game> games) {
        Predicate<Game> defaultP = g ->
                g.state.get() != GameState.SAME && g.state.get() != GameState.BLOCK
                        && !(g.star > 0 && g.star < 3);

        load(games, defaultP);
    }

    private void setSideBarData(FilteredList<Game> filteredGames) {
        dateGroupController.init(filteredGames);
        brandGroupController.init(filteredGames);

        groupCVServ.restart();
        groupTagServ.restart();
    }

    private void loadItems(SortedList<Game> sortedData) {

        tableView.setItems(sortedData);
        tableView.scrollTo(0);

        smallListSimple.setItems(sortedData);
        smallListSimple.scrollTo(0);

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
