package com.goexp.galgame.gui.view.game.detailview;

import com.goexp.common.util.DateUtil;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.util.res.Images;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.view.common.jump.JumpBrandController;
import com.goexp.galgame.gui.view.common.jump.JumpLinkController;
import com.goexp.galgame.gui.view.game.HomeController;
import com.goexp.galgame.gui.view.game.detailview.part.StateChangeChoiceBarController;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


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

            //        if (game.state.get() == GameState.PASS)
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
        private JumpLinkController webjumpController;

        @FXML
        private JumpBrandController brandJumpController;

        @FXML
        private StateChangeChoiceBarController changeStateController;

        @FXML
        private Text txtName;

        @FXML
        private Label lbDate;

        @FXML
        private HBox boxTag;

        protected void initialize() {

        }


        public void load(Game game) {

            webjumpController.load(game);

            txtName.setText(game.name);


            brandJumpController.load(game.brand);

            changeStateController.load(game);


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

            lbDate.setText(DateUtil.formatDate(game.publishDate));

        }
    }
}
