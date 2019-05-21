package com.goexp.galgame.gui.view.game.listview.simplelist.small;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.res.Images;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.game.listview.simplelist.HeaderController;
import javafx.fxml.FXML;

public class CellController extends DefaultController {

    private Game game;

    @FXML
    private HeaderController headerController;


    protected void initialize() {


        //        imageImg.setOnMouseEntered(event -> {
        //            rightPanel.setVisible(true);
        //        });
        //
        //        imageImg.setOnMouseExited(event -> {
        //            rightPanel.setVisible(false);
        //        });
    }


    public void load(Game game) {

        this.game = game;

        headerController.loadWithoutImage(game);

        if (game.smallImg != null && game.smallImg.startsWith("http")) {
            headerController.setImage(Images.GameImage.tiny(game));
        } else {
            headerController.setImage(null);
        }


    }

}
