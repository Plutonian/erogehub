package com.goexp.galgame.gui.view.game.listview.simplelist;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class SimpleListViewController extends DefaultController {

    public static final Callback<ListView<Game>, ListCell<Game>> cellFactory = listView -> {

        final var loader = new FXMLLoaderProxy<Region, CellController>("view/game/listview/cell.fxml");

        return new ListCell<>() {

            @Override
            protected void updateItem(Game item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);

                if (item != null && !empty) {
                    loader.controller.load(item);

                    setGraphic(loader.node);
                }
            }
        };
    };


    @FXML
    private ListView<Game> listSimple;

    protected void initialize() {

        listSimple.setCellFactory(cellFactory);
    }

}
