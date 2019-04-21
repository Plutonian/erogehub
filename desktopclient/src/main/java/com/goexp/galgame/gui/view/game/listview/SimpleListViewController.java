package com.goexp.galgame.gui.view.game.listview;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.*;
import com.goexp.galgame.gui.view.common.jump.JumpBrandController;
import com.goexp.galgame.gui.view.game.HomeController;
import com.goexp.galgame.gui.view.game.detailview.part.DateShowController;
import com.goexp.galgame.gui.view.game.part.StateChangeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.stream.Collectors;

import static com.goexp.galgame.common.util.GameName.NAME_SPLITER_REX;

public class SimpleListViewController extends DefaultController {

    @FXML
    private ListView<Game> listSimple;

    protected void initialize() {

        listSimple.setCellFactory(listView -> {

            final var loader = new FXMLLoaderProxy<Region, CellController>("view/game_explorer/listview/simple_list_cell.fxml");
            logger.debug("Load");

            return new ListCell<>() {

                @Override
                protected void updateItem(Game item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(null);

                    if (item != null && !empty) {
                        loader.controller.load(item);

                        setGraphic(loader.node);
                    }
                }
            };
        });
    }

    public static class CellController extends DefaultController {

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

            headerController.load(game);

            //        if (game.smallImg != null && game.smallImg.startsWith("http")) {
            //
            //            headerPartController.setImage(Images.GameImage.tiny(game, game.smallImg));
            //
            //        } else {
            //            headerPartController.setImage(null);
            //        }
        }

        public static class HeaderController extends DefaultController {

            //    @FXML//    private StarChoiceBarController starChangeController;

            @FXML
            private DateShowController dateviewController;

            //    @FXML//    private JumpLinkController webjumpController;

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
            private FlowPane flowPainter;

            //    @FXML//    public Label lbDate;
            @FXML
            private Label txtWriter;

            @FXML
            private HBox boxTag;

            private Game targetGame;

            protected void initialize() {

                linkView.setOnAction((e) -> {

                    HomeController.$this.loadDetail(targetGame);
                });

                flowPainter.addEventFilter(ActionEvent.ACTION, event -> {
                    if (event.getTarget() instanceof Hyperlink) {

                        var painter = (Hyperlink) event.getTarget();

                        var str = painter.getText().replaceAll("（[^）]+）", "");
                        HomeController.$this.loadPainterTab(str);
                    }
                });
            }


            public void load(Game game) {
                this.targetGame = game;

                loadWithoutImage(game);

                if (game.smallImg != null && game.smallImg.startsWith("http")) {
                    imageImg.setImage(Images.GameImage.small(game));

                } else {
                    imageImg.setImage(null);
                }

                //preload large
                Images.GameImage.large(game);


            }


            public void setImage(Image image) {
                imageImg.setImage(image);
            }

            public void loadWithoutImage(Game game) {
                //        webjumpController.load(game);
                changeStateController.load(game);
                brandJumpController.load(game.brand);
                //        starChangeController.load(game);

                final var matcher = NAME_SPLITER_REX.matcher(game.name);
                final var find = matcher.find();


                txtName.setText(find ? game.name.substring(0, matcher.start()) : game.name);
                txtSubName.setText(find ? game.name.substring(matcher.start()) : "");

                flowPainter.getChildren().setAll(Tags.toNodes(game.painter, str1 -> {
                    var tagLabel1 = new Hyperlink(str1);
                    return tagLabel1;
                }));
                txtWriter.setText(game.writer.stream().collect(Collectors.joining(",")));

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
                var image = LocalRes.HEART_32_PNG.get();
                for (var i = 0; i < game.star; i++) {
                    boxStar.getChildren().add(new ImageView(image));
                }

                if (game.state.get().getValue() <= GameState.BLOCK.getValue())
                    imageImg.setEffect(new ColorAdjust(0, -1, 0, 0));
                else
                    imageImg.setEffect(null);

            }
        }
    }
}
