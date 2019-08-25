package com.goexp.galgame.gui.view.game.listview.sidebar;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.game.panel.group.ByBrand;
import com.goexp.galgame.gui.task.game.panel.group.node.BrandItem;
import com.goexp.galgame.gui.task.game.panel.group.node.CompItem;
import com.goexp.galgame.gui.task.game.panel.group.node.DefaultItem;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;

public class BrandGroupController extends FilterController<Game> {

    @FXML
    private TreeView<DefaultItem> compTree;

    private List<Game> filteredGames;

    private Service<TreeItem<DefaultItem>> groupBrandServ = new TaskService<>(() -> new ByBrand(filteredGames));


    protected void initialize() {
        compTree.setCellFactory(itemNodeTreeView -> {
            return new TreeCell<>() {
                @Override
                protected void updateItem(DefaultItem item, boolean empty) {
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

                if (item.getValue() instanceof CompItem) {
                    predicate = game -> {
                        // target comp can be null,so...change null to empty
                        var comp = game.brand.comp() != null ? game.brand.comp() : "";

                        return comp.equals(((CompItem) item.getValue()).comp);
                    };

                } else {
                    predicate = game -> (game.brand.equals(((BrandItem) item.getValue()).brand));
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
