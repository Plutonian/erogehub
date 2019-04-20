package com.goexp.galgame.gui.view.game.detailview.cell;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.Images;
import com.goexp.galgame.gui.view.game.HomeController;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;


public class GameNavListCellController {

    public Game game;

    @FXML
    private ImageView imageImg;
    @FXML
    private Hyperlink linkTitle;

    public void init() {

        linkTitle.setText(game.name);
        linkTitle.setOnAction(e -> {
            HomeController.$this.loadDetail(game);
        });


        if (game.smallImg != null && game.smallImg.startsWith("http")) {

            imageImg.setImage(Images.GameImage.tiny(game));
        } else {
            imageImg.setImage(null);
        }

//        if (game.state.get() == GameState.PASS)
//            imageImg.setEffect(new ColorAdjust(0, -1, 0, 0));
//        else
//            imageImg.setEffect(null);

    }


}
