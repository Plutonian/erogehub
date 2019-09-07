package com.goexp.galgame.gui.view.game.listview.imglist;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import java.net.URL;

public class ImgListViewController extends DefaultController {

    private static final URL VIEW_GAME_IMGLISTVIEW_IMG_LIST_CELL_FXML = CellController.class.getResource("cell.fxml");
    @FXML
    private GridView<Game> imgList;

    protected void initialize() {
        imgList.setCellFactory(gridView -> {
            final var loader = new FXMLLoaderProxy<Region, CellController>(VIEW_GAME_IMGLISTVIEW_IMG_LIST_CELL_FXML);
            return new GridCell<>() {

                @Override
                protected void updateItem(Game game, boolean empty) {
                    super.updateItem(game, empty);
                    setGraphic(null);

                    if (game != null && !empty) {
                        loader.controller.load(game);

                        setGraphic(loader.node);
                    }

                }
            };

        });
    }

    public void load(ObservableList<Game> item) {
        imgList.setItems(item);
    }
}
