package com.goexp.galgame.gui.view.game.listview.simplelist.small;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.fxml.FXML;

public class CellController extends DefaultController {

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

        headerController.load(game);
    }

}
