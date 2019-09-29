package com.goexp.galgame.gui.view.game.listview.sidebar;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.game.panel.group.ByDate;
import com.goexp.galgame.gui.task.game.panel.group.node.DateItem;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;

public class DateGroupController extends FilterController<Game> {

    @FXML
    private TreeView<DateItem> dateTree;

    private List<Game> filteredGames;

    private final Service<TreeItem<DateItem>> groupDateServ = new TaskService<>(() -> new ByDate(filteredGames));


    protected void initialize() {

        dateTree.setCellFactory(dateItemNodeTreeView -> new TreeCell<>() {

            @Override
            protected void updateItem(DateItem item, boolean empty) {
                super.updateItem(item, empty);

                setGraphic(null);
                setText(null);

                if (!empty && item != null) {
                    setText(item.title);
                }
            }
        });
        dateTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                predicate = game -> game.publishDate != null && game.publishDate.isBefore(newValue.getValue().range.end()) && game.publishDate.isAfter(newValue.getValue().range.start());

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
