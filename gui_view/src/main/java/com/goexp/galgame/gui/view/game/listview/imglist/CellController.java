package com.goexp.galgame.gui.view.game.listview.imglist;

import com.goexp.common.util.DateUtil;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.util.res.Images;
import com.goexp.galgame.gui.util.res.LocalRes;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.game.HomeController;
import com.goexp.galgame.gui.view.game.part.StateChangeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CellController extends DefaultController {

    private Game game;

    @FXML
    private StateChangeController changeStateController;

    @FXML
    private ImageView imageImg;

    @FXML
    private HBox boxStar;

    @FXML
    private Text txtName;

    @FXML
    private Label lbDate;

    @FXML
    private Label lbBrand;

    @FXML
    private FlowPane flowTag;


    protected void initialize() {


    }


    public void load(Game game) {
        this.game = game;

        txtName.setText(game.name);
        lbBrand.setText(game.brand.name);
        lbDate.setText(DateUtil.formatDate(game.publishDate));
        changeStateController.load(game);

        if (game.tag.size() > 0)
            flowTag.getChildren().setAll(Tags.toNodes(game.tag));


        if (game.smallImg != null && game.smallImg.startsWith("http")) {

            imageImg.setImage(Images.GameImage.small(game));

            if (game.state.get() == GameState.BLOCK)
                imageImg.setEffect(new ColorAdjust(0, -1, 0, 0));
            else
                imageImg.setEffect(null);
        }

        var image = LocalRes.HEART_32_PNG.get();
        boxStar.getChildren().clear();
        for (var i = 0; i < game.star; i++) {
            boxStar.getChildren().add(new ImageView(image));
        }

    }

    public void link_OnAction(ActionEvent actionEvent) {
        System.out.println("go");

        HomeController.$this.loadDetail(game);
    }
}