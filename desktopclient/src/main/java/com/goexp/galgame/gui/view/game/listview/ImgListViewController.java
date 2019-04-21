package com.goexp.galgame.gui.view.game.listview;

import com.goexp.common.util.DateUtil;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.*;
import com.goexp.galgame.gui.view.game.HomeController;
import com.goexp.galgame.gui.view.game.part.StateChangeController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.stream.Collectors;

public class ImgListViewController extends DefaultController {

    final private int pageSize = 5;
    @FXML
    private FlowPane imgList;
    @FXML
    private ScrollPane rootPanel;
    private ObservableList<Game> cacheList;
    private int index = 1;
    private int page = 0;


    protected void initialize() {

        rootPanel.vvalueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null && newValue.doubleValue() > 0.8 * rootPanel.getVmax()) {
                loadItem();
            }
        });
    }

    public void load(ObservableList<Game> item) {

        cacheList = item;
        index = 1;
        page = (int) Math.ceil(cacheList.size() / (double) pageSize);
        rootPanel.setVvalue(rootPanel.getVmin());

        imgList.getChildren().clear();

        loadItem();

    }


    private void loadItem() {

        if (index <= page) {

            var nodes = cacheList.stream()
                    .skip((index - 1) * pageSize)
                    .limit(pageSize)
                    .map(game -> {
                        final var loader = new FXMLLoaderProxy<Region, CellController>("view/game_explorer/listview/img/img_list_cell.fxml");

                        loader.controller.load(game);
                        return loader.node;
                    })
                    .collect(Collectors.toUnmodifiableList());

            index++;

            imgList.getChildren().addAll(nodes);
        }

    }

    public static class CellController extends DefaultController {

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

        @FXML
        private Hyperlink linkView;

        protected void initialize() {

            //        rightPanel.setVisible(false);
            //        rightPanel.setManaged(false);
            //
            //        rightPanel.setLayoutX(300);

            linkView.setOnAction((e) -> {

                HomeController.$this.loadDetail(game);
            });

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
    }
}
