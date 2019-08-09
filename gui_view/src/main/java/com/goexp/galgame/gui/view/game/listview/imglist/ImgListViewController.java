package com.goexp.galgame.gui.view.game.listview.imglist;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class ImgListViewController extends DefaultController {

    @FXML
    private GridView<Game> imgList;

    protected void initialize() {
        imgList.setCellFactory(gridView -> {
            final var loader = new FXMLLoaderProxy<Region, CellController>("view/game/imglistview/img_list_cell.fxml");
            return new GridCell<>() {

                @Override
                protected void updateItem(Game game, boolean empty) {


                    super.updateItem(game, empty);
                    setGraphic(null);

                    if (game != null && !empty) {
                        loader.controller.load(game);

                        setGraphic(loader.node);
                    }

//                var imageImg = new ImageView();
//                    if (!empty) {
//                    if (game.smallImg != null && game.smallImg.startsWith("http")) {
//
//                        imageImg.setImage(Images.GameImage.small(game));
//
//                        if (game.state.get() == GameState.BLOCK)
//                            imageImg.setEffect(new ColorAdjust(0, -1, 0, 0));
//                        else
//                            imageImg.setEffect(null);
//                    }
//                    setGraphic(imageImg);
//                    }
                }
            };

        });
    }

    public void load(ObservableList<Game> item) {
        imgList.setItems(item);
    }
}
