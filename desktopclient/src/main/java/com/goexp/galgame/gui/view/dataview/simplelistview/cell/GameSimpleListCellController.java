package com.goexp.galgame.gui.view.dataview.simplelistview.cell;

import com.goexp.galgame.gui.model.Game;
import javafx.fxml.FXML;


public class GameSimpleListCellController {

    private Game game;

    @FXML
    private HeaderController headerController;


    @FXML
    private void initialize() {



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

        headerController.load(game);

//        if (game.smallImg != null && game.smallImg.startsWith("http")) {
//
//            headerController.setImage(Images.GameImage.tiny(game, game.smallImg));
//
//        } else {
//            headerController.setImage(null);
//        }
    }
}
