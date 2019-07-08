package com.goexp.galgame.gui.view.game.listview.sidebar;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.game.panel.PanelTask;
import com.goexp.galgame.gui.task.game.panel.node.DateItemNode;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;

public class DateGroupController extends FilterController<Game> {

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
