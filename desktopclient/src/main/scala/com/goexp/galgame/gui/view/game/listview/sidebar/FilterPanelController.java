package com.goexp.galgame.gui.view.game.listview.sidebar;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.PanelTask;
import com.goexp.galgame.gui.util.TaskService;
import com.goexp.galgame.gui.view.game.listview.sidebar.node.BrandItemNode;
import com.goexp.galgame.gui.view.game.listview.sidebar.node.CompItemNode;
import com.goexp.galgame.gui.view.game.listview.sidebar.node.DateItemNode;
import com.goexp.galgame.gui.view.game.listview.sidebar.node.DefaultItemNode;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FilterPanelController extends FilterController<Game> {

    @FXML
    private FlowPane starbox;

    @FXML
    private FlowPane statebox;

    protected void initialize() {

        reset();
    }

    public void reset() {
        var nodes = Stream.of(GameState.values())
                .sorted(Comparator.comparing(GameState::getValue).reversed())
                .map(gameType -> {
                    var btn = new CheckBox(gameType.getName());
                    btn.setUserData(gameType);

                    if (gameType.getValue() > GameState.BLOCK.getValue())
                        btn.setSelected(true);

                    return btn;
                }).collect(Collectors.toList());

        statebox.getChildren().setAll(nodes);


        var starnodes = IntStream.rangeClosed(0, 5)
                .boxed()
                .sorted(Comparator.comparing(Integer::intValue).reversed())
                .map(star -> {
                    var btn = new CheckBox(star.toString());

                    btn.setSelected(true);

                    btn.setUserData(star);

                    return btn;
                }).collect(Collectors.toList());

        starbox.getChildren().setAll(starnodes);

        setP();
    }

    @FXML
    private void SetFilter_OnAction(ActionEvent event) {
        setP();

        onSetProperty.set(true);
        onSetProperty.set(false);
    }

    private void setP() {
        var stars = starbox.getChildren().stream()
                .filter(node -> ((CheckBox) node).isSelected())
                .map(node -> Integer.parseInt(((CheckBox) node).getText()))
                .collect(Collectors.toSet());

        var states = statebox.getChildren().stream()
                .filter(node -> ((CheckBox) node).isSelected())
                .map(node -> (GameState) (node.getUserData()))
                .collect(Collectors.toSet());

        if (stars.isEmpty())
            stars.add(0);

        predicate = game -> stars.contains(game.star) && states.contains(game.state.get());
    }

    @Override
    public void init(List<Game> filteredGames) {

    }

    public static class BrandGroupController extends FilterController<Game> {

        @FXML
        private TreeView<DefaultItemNode> compTree;

        private List<Game> filteredGames;

        private Service<TreeItem<DefaultItemNode>> groupBrandServ = new TaskService<>(() -> new PanelTask.GroupBrand(filteredGames));


        protected void initialize() {
            compTree.setCellFactory(itemNodeTreeView -> {
                return new TreeCell<>() {
                    @Override
                    protected void updateItem(DefaultItemNode item, boolean empty) {
                        super.updateItem(item, empty);

                        setGraphic(null);
                        setText(null);

                        if (!empty && item != null) {

                            setText(String.format("%s (%d)", item.title, item.count));
                        }
                    }
                };
            });
            compTree.getSelectionModel().selectedItemProperty().addListener((o, old, item) -> {
                if (item != null) {

                    if (item.getValue() instanceof CompItemNode) {
                        predicate = game -> {
                            // target comp can be null,so...change null to empty
                            var comp = game.brand.comp != null ? game.brand.comp : "";

                            return comp.equals(((CompItemNode) item.getValue()).comp);
                        };

                    } else {
                        predicate = game -> (game.brand.equals(((BrandItemNode) item.getValue()).brand));
                    }

                    onSetProperty.set(true);
                    onSetProperty.set(false);
                }
            });

            groupBrandServ.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    compTree.setRoot(newValue);
                }
            });

        }

        @Override
        public void init(List<Game> filteredGames) {
            this.filteredGames = filteredGames;

            groupBrandServ.restart();

        }


    }

    public static class DateGroupController extends FilterController<Game> {

        @FXML
        private TreeView<DateItemNode> dateTree;

        private List<Game> filteredGames;

        private Service<TreeItem<DateItemNode>> groupDateServ = new TaskService<>(() -> new PanelTask.GroupDate(filteredGames));


        protected void initialize() {

            dateTree.setCellFactory(dateItemNodeTreeView -> {
                return new TreeCell<>() {

                    @Override
                    protected void updateItem(DateItemNode item, boolean empty) {
                        super.updateItem(item, empty);

                        setGraphic(null);
                        setText(null);

                        if (!empty && item != null) {
                            setText(item.title);
                        }
                    }
                };
            });
            dateTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

                if (newValue != null) {
                    predicate = game -> game.publishDate != null && game.publishDate.isBefore(newValue.getValue().range.end) && game.publishDate.isAfter(newValue.getValue().range.start);

                    onSetProperty.set(true);
                    onSetProperty.set(false);
                }
            });

            groupDateServ.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    dateTree.setRoot(newValue);
                }
            });
        }

        public void init(List<Game> filteredGames) {
            this.filteredGames = filteredGames;

            groupDateServ.restart();
        }
    }


}
