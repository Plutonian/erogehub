package com.goexp.galgame.gui.view.dataview.simplelistview;

import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.dataview.simplelistview.cell.GameSimpleListCellController;
import com.goexp.galgame.gui.model.Game;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class GameSimpleListController {


    @FXML
    private ListView<Game> listSimple;

    @FXML
    private void initialize() {

        listSimple.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Game> call(ListView<Game> param) {

                // Must call super
                //super.updateItem(item, empty);
                return new ListCell<>() {
                    private static final String SIMPLE_LIST_CELL_FXML = "view/game_explorer/listview/simple_list_cell.fxml";

                    @Override
                    protected void updateItem(Game item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(null);

                        if (item == null || empty) {
                        } else {

                            var loader = new FXMLLoaderProxy(SIMPLE_LIST_CELL_FXML);
                            var node = (Region) loader.load();
                            var controller = (GameSimpleListCellController) loader.getController();
                            controller.load(item);

                            setGraphic(node);
                        }

                    }
                };
            }
        });

//        listSimple.setCellFactory(new Callback<>() {
//            @Override
//            public ListCell<Game> call(ListView<Game> param) {
//
//                // Must call super
//                //super.updateItem(item, empty);
//                return new ListCell<>() {
//
//                    @Override
//                    protected void updateItem(Game item, boolean empty) {
//                        super.updateItem(item, empty);
//                        setGraphic(null);
//                        setText(null);
//
//                        if (item == null || empty) {
//                        } else {
//
//                            setText(item.name);
//                        }
//
//                    }
//                };
//            }
//        });
    }


}
