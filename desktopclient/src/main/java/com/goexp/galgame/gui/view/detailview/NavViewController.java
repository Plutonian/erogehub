package com.goexp.galgame.gui.view.detailview;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.detailview.header.SmallHeaderController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NavViewController {
    private final Logger logger = LoggerFactory.getLogger(NavViewController.class);


    @FXML
    public FullContentViewController rootContainerController;

    @FXML
    public SmallHeaderController headerController;


    private Game game;

    @FXML
    private Region header;

    @FXML
    private VBox vboxNav;

//    @FXML
//    private ListView<Game> listNav;

    private Timeline timeline = new Timeline();

//    private Service<ObservableList<Game>> gameListByBrandService = new Service<>() {
//        @Override
//        protected Task<ObservableList<Game>> createTask() {
//            return new BrandGetGameTask.ByBrand(game.brand.id);
//        }
//    };

    @FXML
    private void initialize() {

//        gameListByBrandService.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//
//
//                listNav.setItems(newValue);
//                listNav.getSelectionModel().select(game);
//                listNav.scrollTo(game);
//            }
//
//        });
//        gameListByBrandService.exceptionProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null)
//                newValue.printStackTrace();
//        });

//        listNav.setCellFactory(new Callback<>() {
//
//            @Override
//            public ListCell<Game> call(ListView<Game> param) {
//
//                // Must call super
//                //super.updateItem(item, empty);
//                return new ListCell<>() {
//                    private static final String NAV_LIST_CELL_FXML = "view/game_explorer/detail/nav_list_cell.fxml";
//
//                    @Override
//                    protected void updateItem(Game item, boolean empty) {
//                        super.updateItem(item, empty);
//
//
//                        if (item == null || empty) {
//                            setGraphic(null);
//                            setText(null);
//                        } else {
//
//                            var loader = new FXMLLoaderProxy(NAV_LIST_CELL_FXML);
//
//                            var node = (Region) loader.load();
//                            var controller = (GameNavListCellController) loader.getController();
//                            controller.game = item;
//                            controller.init();
//
//                            setGraphic(node);
//
//                        }
//
//                    }
//                };
//            }
//        });

        ((VBox) (rootContainerController.rootContainer.getContent()))
                .getChildren()
                .stream()
                .filter(node -> node instanceof TitledPane)
                .forEach(node -> {
                    TitledPane pane = (TitledPane) node;
                    var link = new Hyperlink();
                    link.setText(pane.getText());
                    link.setUserData(pane);
                    link.setOnAction(event -> {
                        var l = (Hyperlink) event.getSource();
                        var p = (TitledPane) l.getUserData();


                        final var h = rootContainerController.rootContainer
                                .getContent()
                                .getLayoutBounds()
                                .getHeight()
                                - rootContainerController.rootContainer
                                .getViewportBounds()
                                .getHeight();

                        timeline.stop();
                        timeline.getKeyFrames().setAll(
                                new KeyFrame(Duration.millis(200),
                                        new KeyValue(rootContainerController.rootContainer.vvalueProperty(), (p.getLayoutY() - header.getHeight()) / h))
                        );
                        timeline.play();
                    });

                    vboxNav.getChildren().add(link);
                });


        rootContainerController.rootContainer.vvalueProperty().addListener((o, old, newV) -> {
            final var h = rootContainerController.rootContainer
                    .getContent()
                    .getLayoutBounds()
                    .getHeight()
                    - rootContainerController.rootContainer
                    .getViewportBounds()
                    .getHeight();

            var v = rootContainerController.introPanel.getLayoutY() / h;

            if (newV.doubleValue() > v) {
                header.setVisible(true);
            } else {
                header.setVisible(false);
            }

        });


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


    @FXML
    private void linkTop_OnAction(ActionEvent event) {
        timeline.stop();
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(rootContainerController.rootContainer.vvalueProperty(), 0))
        );
        timeline.play();
    }

}
