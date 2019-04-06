package com.goexp.galgame.gui.view.dataview.simplelistview;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.dataview.simplelistview.cell.GameSimpleListCellController;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameSimpleListController {

    @FXML
    private ListView<Game> listSimple;

    @FXML
    private void initialize() {

        listSimple.setCellFactory(listView -> {

            final Logger logger = LoggerFactory.getLogger(ListCell.class);
            final var loader = new FXMLLoaderProxy<Region, GameSimpleListCellController>("view/game_explorer/listview/simple_list_cell.fxml");
            logger.debug("Load");

            return new ListCell<>() {

                @Override
                protected void updateItem(Game item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(null);

                    if (item == null || empty) {
                    } else {
                        loader.controller.load(item);

                        setGraphic(loader.node);
                    }
                }
            };
        });
    }

}
