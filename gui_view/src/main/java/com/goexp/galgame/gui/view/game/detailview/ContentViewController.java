package com.goexp.galgame.gui.view.game.detailview;

import com.goexp.common.util.Strings;
import com.goexp.galgame.common.model.CommonGame;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.util.res.Images;
import com.goexp.galgame.gui.util.res.LocalRes;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.common.jump.JumpBrandController;
import com.goexp.galgame.gui.view.game.HomeController;
import com.goexp.galgame.gui.view.game.detailview.part.CVSearchController;
import com.goexp.galgame.gui.view.game.detailview.part.DateShowController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

import static com.goexp.galgame.common.util.GameName.NAME_SPLITER_REX;


public class ContentViewController extends DefaultController {

    @FXML
    public HeaderPartController headerController;

    @FXML
    public SimpleImgPartController simpleImgController;
    public ScrollPane rootContainer;
    private Game game;
    @FXML
    private ListView<CommonGame.GameCharacter> personListView;

    @FXML
    private TabPane contentTabPane;
    @FXML
    private Tab tabPerson;
    @FXML
    private Tab tabSimple;


//    private Service<ObservableList<Game.GameCharacter>> charListByGameService = new TaskService<>(() -> new GameCharListTask(game.id));


    protected void initialize() {

        personListView.setCellFactory(listChar -> {

            var loader = new FXMLLoaderProxy<Region, PersonCellController>("view/game/detail/part/person_cell.fxml");

            return new ListCell<>() {
                @Override
                protected void updateItem(CommonGame.GameCharacter gameCharacter, boolean empty) {
                    super.updateItem(gameCharacter, empty);
                    setText(null);
                    setGraphic(null);

                    if (gameCharacter != null && !empty) {
                        var controller = loader.controller;
                        controller.game = game;
                        controller.gameChar = gameCharacter;
                        controller.init();
                        setGraphic(loader.node);
                    }
                }
            };
        });


//        charListByGameService.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                persionListView.setItems(newValue);
//            }
//        });

    }


    public void load(Game game) {


        rootContainer.setVvalue(0);

        this.game = game;

        headerController.load(game);

        /**
         * person
         */
        var personSize = Optional.ofNullable(game.gameCharacters).map(List::size).orElse(0);

        if (personSize == 0) {
            contentTabPane.getTabs().remove(tabPerson);
        } else {
            personListView.setItems(FXCollections.observableList(game.gameCharacters));
        }


        var imgsSize = Optional.ofNullable(game.gameImgs).map(List::size).orElse(0);

        if (imgsSize == 0) {
            contentTabPane.getTabs().remove(tabSimple);
        } else {
            simpleImgController.load(game);
        }


        logger.debug("{}", game);


    }

    public static class HeaderPartController extends DefaultController {

//        @FXML
//        private StarChoiceBarController starChangeController;

        @FXML
        private DateShowController dateviewController;

//        @FXML
//        private JumpLinkController webjumpController;

        @FXML
        private JumpBrandController brandJumpController;

//        @FXML
//        private StateChangeController changeStateController;

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

        @FXML
        private Text txtIntro;

        @FXML
        private Text txtStory;

        private Game targetGame;

        protected void initialize() {

            flowPainter.addEventFilter(ActionEvent.ACTION, event -> {
                if (event.getTarget() instanceof Hyperlink) {

                    var painter = (Hyperlink) event.getTarget();

                    var str = painter.getText().replaceAll("（[^）]+）", "");
                    HomeController.$this.loadPainterTab(str);
                }
            });

//            starChangeController.onStarChangeProperty.addListener((observable, oldValue, changed) -> {
//
//                if (changed) {
//                    loadStar(targetGame);
//                }
//            });
        }


        public void load(Game game) {
            this.targetGame = game;

            loadWithoutImage(game);

            if (game.smallImg != null && game.smallImg.startsWith("http")) {
                imageImg.setImage(Images.GameImage.large(game));
            } else {
                imageImg.setImage(null);
            }
        }


        public void setImage(Image image) {
            imageImg.setImage(image);
        }

        private void loadWithoutImage(Game game) {
            this.targetGame = game;

//            webjumpController.load(game);
//            changeStateController.load(game);
            brandJumpController.load(game.brand);
//            starChangeController.load(game);

            var matcher = NAME_SPLITER_REX.matcher(game.name);
            final var find = matcher.find();


            txtName.setText(find ? game.name.substring(0, matcher.start()) : game.name);
            txtSubName.setText(find ? game.name.substring(matcher.start()) : "");

            flowPainter.getChildren().setAll(Tags.toNodes(game.painter, Hyperlink::new));
            txtWriter.setText(String.join(",", game.writer));

            if (Strings.isNotEmpty(game.intro))
                txtIntro.setText(game.intro + "\n\n");

            txtStory.setText(game.story);

            if (game.tag.size() > 0) {
                var nodes = Tags.toNodes(game.tag, str -> {
                    var tagLabel = new Label(str);
                    tagLabel.getStyleClass().add("tag");
                    tagLabel.getStyleClass().add("tagbig");

                    return tagLabel;
                });

                boxTag.getChildren().setAll(nodes);
            } else {
                boxTag.getChildren().clear();
            }

            dateviewController.load(game.publishDate);

            loadStar(game);

            if (game.state.get().value <= GameState.BLOCK.value)
                imageImg.setEffect(new ColorAdjust(0, -1, 0, 0));
            else
                imageImg.setEffect(null);

        }

        public void loadStar(Game game) {
            boxStar.getChildren().clear();
            var image = LocalRes.HEART_32_PNG.get();
            for (var i = 0; i < game.star; i++) {
                boxStar.getChildren().add(new ImageView(image));
            }
        }
    }

    public static class PersonCellController extends DefaultController {
        Game.GameCharacter gameChar;

        private Game game;

        @FXML
        private CVSearchController cvsearchController;

        @FXML
        private ImageView imageImg;

        @FXML
        private Text txtName;

        @FXML
        private MenuButton lbCV;

        @FXML
        private MenuItem truecv;

        @FXML
        private Text txtIntro;

        @FXML
        private Region cvPart;


        public void init() {


            logger.debug("{}", gameChar);

            txtName.setText(gameChar.name);


            boolean isTrueCV = gameChar.trueCV != null && gameChar.trueCV.length() > 0;

            var cv = isTrueCV ? "*" + gameChar.trueCV + "*" : gameChar.cv;

            if (cv != null && cv.length() > 0) {
                cvPart.setVisible(true);
                cvsearchController.load(cv);
                lbCV.setText(cv);

                truecv.setOnAction(event -> {
                    HomeController.$this.loadCVTab(isTrueCV ? gameChar.trueCV : gameChar.cv, isTrueCV);
                });
            } else {
                cvPart.setVisible(false);
            }


            if (gameChar.intro == null || gameChar.intro.trim().length() == 0) {
                txtIntro.setVisible(false);
            } else
                txtIntro.setText(gameChar.intro);


            if (gameChar.img != null && gameChar.img.length() > 0) {

                imageImg.setImage(Images.GameImage.GameChar.small(game, gameChar.index, gameChar.img));
            } else {
                imageImg.setImage(null);
            }

        }

        @Override
        protected void initialize() {

        }
    }

    public static class SimpleImgPartController extends DefaultController {

        private Game game;

        @FXML
        private ListView<Game.GameImg> listSmallSimple;

        @FXML
        private ImageView largeSimple;


//        private Service<ObservableList<Game.GameImg>> imgListService = new TaskService<>(() -> new GameImgListTask(game.id));


        protected void initialize() {


//            imgListService.valueProperty().addListener((observable, oldValue, newValue) -> {
//                if (newValue != null) {
//                    listSmallSimple.setItems(FXCollections.observableArrayList(newValue));
//
//                    if (newValue.size() > 0) {
//                        listSmallSimple.getSelectionModel().select(0);
//                    }
//                }
//            });

            listSmallSimple.setCellFactory(gameImgListView -> {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Game.GameImg item, boolean empty) {
                        super.updateItem(item, empty);

                        setGraphic(null);
                        setText(null);

                        if (!empty) {
                            setGraphic(new ImageView(Images.GameImage.Simple.small(game, item.index, item.src)));
                        }

                    }
                };
            });

            listSmallSimple.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, simpleLargeImage) -> {

                if (simpleLargeImage != null) {
                    largeSimple.setImage(Images.GameImage.Simple.large(game, simpleLargeImage.index, simpleLargeImage.src));
                }
            });

        }


        public void load(Game game) {

            this.game = game;
            listSmallSimple.getSelectionModel().clearSelection();
            largeSimple.setImage(null);

            listSmallSimple.setItems(FXCollections.observableArrayList(game.gameImgs));
            listSmallSimple.getSelectionModel().select(0);

            logger.debug("{}", game);

        }


        @FXML
        private void btnPrev_OnAction(ActionEvent event) {
            var index = listSmallSimple.getSelectionModel().getSelectedIndex();
            index--;
            if (index >= 0) {
                listSmallSimple.getSelectionModel().select(index);
                listSmallSimple.scrollTo(index);
            }
        }

        @FXML
        private void btnNext_OnAction(ActionEvent event) {
            var index = listSmallSimple.getSelectionModel().getSelectedIndex();
            index++;
            if (index < listSmallSimple.getItems().size()) {
                listSmallSimple.getSelectionModel().select(index);
                listSmallSimple.scrollTo(index);
            }
        }

    }
}
