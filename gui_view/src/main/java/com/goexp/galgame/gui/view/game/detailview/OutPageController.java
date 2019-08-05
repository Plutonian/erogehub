package com.goexp.galgame.gui.view.game.detailview;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.res.Images;
import com.goexp.galgame.gui.util.res.LocalRes;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.common.jump.JumpLinkController;
import com.goexp.galgame.gui.view.game.detailview.part.StarChoiceBarController;
import com.goexp.galgame.gui.view.game.part.StateChangeController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


public class OutPageController extends DefaultController {

    @FXML
    public ContentViewController rootContainerController;

    @FXML
    public ControllBarController headerController;


    public ImageView imgBackground;

    private Game game;

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

        loadBackgroundImage(game);

        loadInnerPage(game);

        logger.info("Detail:{}", game);


    }

    private void loadBackgroundImage(Game game) {
        if (game.smallImg != null && game.smallImg.startsWith("http")) {
            imgBackground.setImage(Images.GameImage.large(game));
        } else {
            imgBackground.setImage(null);
        }
    }

    public void loadInnerPage(Game g) {

        rootContainerController.load(g);

        headerController.load(g);

    }

    public static class ControllBarController extends DefaultController {

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
