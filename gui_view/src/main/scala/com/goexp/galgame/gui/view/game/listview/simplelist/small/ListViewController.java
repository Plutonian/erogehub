package com.goexp.galgame.gui.view.game.listview.simplelist.small;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;


public class ListViewController extends DefaultController {

    @FXML
    private ListView<Game> smallListSimple;

    protected void initialize() {

        smallListSimple.setCellFactory(listView -> {

            final var loader = new FXMLLoaderProxy<Region, HeaderController>("view/game/listview/small/header.fxml");

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
        });
    }

}
