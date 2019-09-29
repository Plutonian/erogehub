package com.goexp.galgame.gui.view.game.detailview.outer;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.common.jump.JumpLinkController;
import com.goexp.galgame.gui.view.game.part.StateChangeController;
import javafx.fxml.FXML;

public class ControllBarController extends DefaultController {


    @FXML
    private JumpLinkController webjumpController;

    @FXML
    private StateChangeController changeStateController;

//    public ImageView imageImg;

    protected void initialize() {

    }

    public void load(Game game) {

        loadWithoutImage(game);

//            if (game.smallImg != null && game.smallImg.startsWith("http")) {
//                imageImg.setImage(Images.GameImage.tiny(game));
//
//            } else {
//                imageImg.setImage(null);
//            }

    }

    private void loadWithoutImage(Game game) {

        changeStateController.load(game);
        webjumpController.load(game);

    }
}
