package com.goexp.galgame.gui.view.dataview.imglistview;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.util.Images;
import com.goexp.galgame.gui.util.UIUtil;
import com.goexp.common.util.DateUtil;
import com.goexp.galgame.gui.view.common.StateChangeController;
import com.goexp.galgame.gui.view.common.jump.JumpLinkController;
import com.goexp.galgame.gui.util.LocalRes;
import com.goexp.galgame.gui.view.search.MainSearchController;
import com.goexp.galgame.gui.model.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


public class GameImgListCellController {

    public Game game;

    @FXML
    private StateChangeController changeStateController;

    @FXML
    private JumpLinkController webjumpController;

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

    @FXML
    private Hyperlink linkView;

    @FXML
    private void initialize() {

//        rightPanel.setVisible(false);
//        rightPanel.setManaged(false);
//
//        rightPanel.setLayoutX(300);

        linkView.setOnAction((e) -> {

            MainSearchController.$this.loadDetail(game);
        });

//        imageImg.setOnMouseEntered(event -> {
//            rightPanel.setVisible(true);
//        });
//
//        imageImg.setOnMouseExited(event -> {
//            rightPanel.setVisible(false);
//        });
    }


    public void load() {
        txtName.setText(game.name);
        lbBrand.setText(game.brand.name);
        lbDate.setText(DateUtil.formatDate(game.publishDate));
        changeStateController.load(game);
        webjumpController.load(game);

        if (game.tag.size() > 0)
            flowTag.getChildren().setAll(UIUtil.createTag(game.tag));


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
}
