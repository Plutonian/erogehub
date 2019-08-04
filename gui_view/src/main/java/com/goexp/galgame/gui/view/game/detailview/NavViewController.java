package com.goexp.galgame.gui.view.game.detailview;

import com.goexp.common.util.DateUtil;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.res.LocalRes;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.util.res.Images;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.view.common.jump.JumpBrandController;
import com.goexp.galgame.gui.view.common.jump.JumpLinkController;
import com.goexp.galgame.gui.view.game.HomeController;
import com.goexp.galgame.gui.view.game.detailview.part.DateShowController;
import com.goexp.galgame.gui.view.game.detailview.part.StarChoiceBarController;
import com.goexp.galgame.gui.view.game.detailview.part.StateChangeChoiceBarController;
import com.goexp.galgame.gui.view.game.part.StateChangeController;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static com.goexp.galgame.common.util.GameName.NAME_SPLITER_REX;


public class NavViewController extends DefaultController {

    @FXML
    public ContentViewController rootContainerController;

    @FXML
    public SmallHeaderController headerController;


    private Game game;

    @FXML
    private Region header;

    @FXML
    private VBox vboxNav;


    private Timeline timeline = new Timeline();


    protected void initialize() {

//        ((VBox) (rootContainerController.rootContainer.getContent()))
//                .getChildren()
//                .stream()
//                .filter(node -> node instanceof TitledPane)
//                .forEach(node -> {
//                    TitledPane pane = (TitledPane) node;
//                    var link = new Hyperlink();
//                    link.setText(pane.getText());
//                    link.setUserData(pane);
//                    link.setOnAction(event -> {
//                        var l = (Hyperlink) event.getSource();
//                        var p = (TitledPane) l.getUserData();
//
//
//                        final var h = rootContainerController.rootContainer
//                                .getContent()
//                                .getLayoutBounds()
//                                .getHeight()
//                                - rootContainerController.rootContainer
//                                .getViewportBounds()
//                                .getHeight();
//
//                        timeline.stop();
//                        timeline.getKeyFrames().setAll(
//                                new KeyFrame(Duration.millis(200),
//                                        new KeyValue(rootContainerController.rootContainer.vvalueProperty(), (p.getLayoutY() - header.getHeight()) / h))
//                        );
//                        timeline.play();
//                    });
//
//                    vboxNav.getChildren().add(link);
//                });
//
//
//        rootContainerController.rootContainer.vvalueProperty().addListener((o, old, newV) -> {
//            final var h = rootContainerController.rootContainer
//                    .getContent()
//                    .getLayoutBounds()
//                    .getHeight()
//                    - rootContainerController.rootContainer
//                    .getViewportBounds()
//                    .getHeight();
//
//            var v = rootContainerController.introPanel.getLayoutY() / h;
//
//            if (newV.doubleValue() > v) {
//                header.setVisible(true);
//            } else {
//                header.setVisible(false);
//            }
//
//        });


    }


    public void load(Game game) {
        this.game = game;
//        listNav.getSelectionModel().clearSelection();
//        gameListByBrandService.restart();

//
//        var types = Stream.of(
//                BrandType.values()
//        )
//                .filter(t -> t != BrandType.ALL)
//                .collect(Collectors.toList());
//
//        choiceLike.setItems(FXCollections.observableArrayList(types));
//        choiceLike.setConverter(new StringConverter<>() {
//            public String toString(BrandType object) {
//                return object.getName();
//            }
//
//            public BrandType fromString(String string) {
//                return BrandType.from(string);
//            }
//        });


//        listener = (o, oldValue, newValue) -> {
//
//            if (newValue != null) {
//                logger.debug("<Action>Value:{},frombrand:{}", choiceLike.getValue(), frombrand.state);
//
//                changeGameStateService.restart();
//            }
//        };


        loadDetailPage(game);

        logger.info("Detail:{}", game);


    }

    public void loadDetailPage(Game g) {

        rootContainerController.load(g);

        headerController.load(g);

    }

    public static class CellController extends DefaultController {

        public Game game;

        @FXML
        private ImageView imageImg;
        @FXML
        private Hyperlink linkTitle;

        public void init() {

            linkTitle.setText(game.name);
            linkTitle.setOnAction(e -> {
                HomeController.$this.loadDetail(game);
            });


            if (game.smallImg != null && game.smallImg.startsWith("http")) {

                imageImg.setImage(Images.GameImage.tiny(game));
            } else {
                imageImg.setImage(null);
            }

            //        if (game.state.get() == GameState.BLOCK)
            //            imageImg.setEffect(new ColorAdjust(0, -1, 0, 0));
            //        else
            //            imageImg.setEffect(null);

        }

        @Override
        protected void initialize() {

        }
    }

    public static class SmallHeaderController extends DefaultController {

        @FXML
        private StarChoiceBarController starChangeController;

        @FXML
        private JumpLinkController webjumpController;

        @FXML
        private StateChangeController changeStateController;

        @FXML
        private HBox boxStar;

        private Game targetGame;

        protected void initialize() {

            starChangeController.onStarChangeProperty.addListener((observable, oldValue, changed) -> {

                if (changed) {
                    loadStar(targetGame);
                }
            });
        }

        public void loadStar(Game game) {
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

            boxStar.getChildren().clear();
            var image = LocalRes.HEART_16_PNG.get();
            for (var i = 0; i < game.star; i++) {
                boxStar.getChildren().add(new ImageView(image));
            }

        }
    }
}
