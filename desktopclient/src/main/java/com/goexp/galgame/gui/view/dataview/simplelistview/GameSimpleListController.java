package com.goexp.galgame.gui.view.dataview.simplelistview;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.Cache;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.dataview.simplelistview.cell.GameSimpleListCellController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class GameSimpleListController {

    @FXML
    private ListView<Game> listSimple;

    @FXML
    private void initialize() {

        listSimple.setCellFactory(new Callback<>() {

            private final Cache<Integer, Node> cache = new Cache<>();
            private final Logger logger = LoggerFactory.getLogger(Callback.class);

            @Override
            public ListCell<Game> call(ListView<Game> param) {

                return new ListCell<>() {
                    private static final String SIMPLE_LIST_CELL_FXML = "view/game_explorer/listview/simple_list_cell.fxml";

                    @Override
                    protected void updateItem(Game item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(null);

                        if (item == null || empty) {
                        } else {

                            var tnode = Optional.ofNullable(cache.get(item.id)).orElseGet(() -> {

                                logger.debug("Load Node");


                                var loader = new FXMLLoaderProxy(SIMPLE_LIST_CELL_FXML);
                                var node = (Region) loader.load();
                                var controller = (GameSimpleListCellController) loader.getController();
                                controller.load(item);

                                cache.put(item.id, node);

                                return node;
                            });

                            setGraphic(tnode);
                        }

                    }
                };
            }
        });
    }


}
