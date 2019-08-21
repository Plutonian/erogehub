package com.goexp.galgame.gui.view.game.detailview.outer;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.util.res.LocalRes;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.common.jump.JumpBrandController;
import com.goexp.galgame.gui.view.common.jump.JumpLinkController;
import com.goexp.galgame.gui.view.game.detailview.part.StarChoiceBarController;
import com.goexp.galgame.gui.view.game.part.StateChangeController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ControllBarController extends DefaultController {

    @FXML
    private StarChoiceBarController starChangeController;

    @FXML
    private JumpLinkController webjumpController;

    @FXML
    private StateChangeController changeStateController;

    @FXML
    private JumpBrandController brandJumpController;

    @FXML
    private HBox boxStar;

    @FXML
    private HBox boxTag;

    @FXML
    private Text txtName;

    @FXML
    private Text txtSubName;

    private Game targetGame;

    protected void initialize() {

        starChangeController.onStarChangeProperty.addListener((observable, oldValue, changed) -> {

            if (changed) {
                loadStar(targetGame);
            }
        });
    }

    private void loadStar(Game game) {
        boxStar.getChildren().clear();
        var image = LocalRes.HEART_16_PNG.get();
        for (var i = 0; i < game.star; i++) {
            boxStar.getChildren().add(new ImageView(image));
        }
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
        this.targetGame = game;

        changeStateController.load(game);
        starChangeController.load(game);
        webjumpController.load(game);

        txtName.setText(game.getMainName());
        txtSubName.setText(game.getSubName());


        brandJumpController.load(game.brand);

        if (game.tag.size() > 0) {
            var nodes = Tags.toNodes(game.tag, str -> {
                var tagLabel = new Label(str);
                tagLabel.getStyleClass().add("tag");
                tagLabel.getStyleClass().add("tagbig");

                return tagLabel;
            });

            boxTag.getChildren().setAll(nodes);
        } else {
            boxTag.getChildren().clear();
        }

        boxStar.getChildren().clear();
        var image = LocalRes.HEART_16_PNG.get();
        for (var i = 0; i < game.star; i++) {
            boxStar.getChildren().add(new ImageView(image));
        }

    }
}
