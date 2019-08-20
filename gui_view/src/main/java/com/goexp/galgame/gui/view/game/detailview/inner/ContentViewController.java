package com.goexp.galgame.gui.view.game.detailview.inner;

import com.goexp.galgame.common.model.CommonGame;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.util.res.Images;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

import java.util.List;
import java.util.Optional;


public class ContentViewController extends DefaultController {

    @FXML
    public HeaderPartController headerController;

    @FXML
    public SimpleImgPartController simpleImgController;
    public ImageView imgLarge;
    //    public ScrollPane rootContainer;
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

        if (game.smallImg != null && game.smallImg.startsWith("http")) {
            imgLarge.setImage(Images.GameImage.large(game));
        } else {
            imgLarge.setImage(null);
        }

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

}
