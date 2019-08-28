package com.goexp.galgame.gui.view.game.detailview.outer;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.res.gameimg.GameImage;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.game.detailview.inner.ContentViewController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;


public class OutPageController extends DefaultController {

    @FXML
    public ContentViewController innerPageController;

    @FXML
    public ControllBarController headerController;

    @FXML
    public TopController topController;


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

//        loadBackgroundImage(game);

        loadInnerPage(game);

        logger.info("Detail:{}", game);


    }

    private void loadBackgroundImage(Game game) {
        if (game.isOkImg()) {
            imgBackground.setImage(new GameImage(game).large());
        } else {
            imgBackground.setImage(null);
        }
    }

    public void loadInnerPage(Game g) {

        topController.load(g);
        innerPageController.load(g);
        headerController.load(g);

    }

}
