package com.goexp.galgame.gui.view.game.listview.simplelist.small;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.util.res.Images;
import com.goexp.galgame.gui.util.res.LocalRes;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.common.jump.JumpBrandController;
import com.goexp.galgame.gui.view.game.HomeController;
import com.goexp.galgame.gui.view.game.detailview.part.DateShowController;
import com.goexp.galgame.gui.view.game.part.StateChangeController;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class HeaderController extends DefaultController {

    @FXML
    private DateShowController dateviewController;

    @FXML
    private Hyperlink linkView;


    @FXML
    private JumpBrandController brandJumpController;

    @FXML
    private StateChangeController changeStateController;

    @FXML
    private ImageView imageImg;

    @FXML
    private HBox boxStar;

    @FXML
    private Text txtName;

    @FXML
    private Text txtSubName;


    @FXML
    private HBox boxTag;

    private Game targetGame;

    protected void initialize() {

        linkView.setOnAction((e) -> {

            HomeController.$this.loadDetail(targetGame);
        });

    }


    public void load(Game game) {


        loadWithoutImage(game);

        if (game.smallImg != null && game.smallImg.startsWith("http")) {
            imageImg.setImage(Images.GameImage.tiny(game));

        } else {
            imageImg.setImage(null);
        }


    }


    public void setImage(Image image) {
        imageImg.setImage(image);
    }

    private void loadWithoutImage(Game game) {
        this.targetGame = game;


        //        webjumpController.load(game);
        changeStateController.load(game);
        brandJumpController.load(game.brand);
        //        starChangeController.load(game);


        txtName.setText(game.getMainName());
        txtSubName.setText(game.getSubName());

        if (game.tag.size() > 0) {

            boxTag.getChildren().setAll(Tags.toNodes(game.tag, str -> {
                var tagLabel = new Label(str);

                tagLabel.getStyleClass().add("tag");
                tagLabel.getStyleClass().add("tagbig");

                return tagLabel;
            }));
        } else {
            boxTag.getChildren().clear();
        }

        dateviewController.load(game.publishDate);

        boxStar.getChildren().clear();
        var image = LocalRes.HEART_16_PNG.get();
        for (var i = 0; i < game.star; i++) {
            boxStar.getChildren().add(new ImageView(image));
        }

        if (game.state.get().value <= GameState.BLOCK.value)
            imageImg.setEffect(new ColorAdjust(0, -1, 0, 0));
        else
            imageImg.setEffect(null);

    }
}
