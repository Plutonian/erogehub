package com.goexp.galgame.gui.view.game.detailview;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.game.detailview.cell.GameCharListCellController;
import com.goexp.galgame.gui.view.game.detailview.header.HeaderController;
import com.goexp.galgame.gui.task.GameCharListTask;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;


public class FullContentViewController {
    private final Logger logger = LoggerFactory.getLogger(FullContentViewController.class);

    @FXML
    public HeaderController headerController;

    @FXML
    public SimpleImgController simpleImgController;
    public ScrollPane rootContainer;
    public Region introPanel;
    private Game game;

    @FXML
    private Text txtIntro;

    @FXML
    private Text txtStory;

    @FXML
    private VBox listChar;


    private Service<ObservableList<Game.GameCharacter>> charListByGameService = new Service<>() {
        @Override
        protected Task<ObservableList<Game.GameCharacter>> createTask() {
            return new GameCharListTask(game.id);
        }
    };

    @FXML
    private void initialize() {

        ChangeListener<Throwable> exceptionListener = ((observable, oldValue, newValue) -> {
            if (newValue != null)
                newValue.printStackTrace();
        });

        charListByGameService.exceptionProperty().addListener(exceptionListener);
        charListByGameService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                var list = newValue.stream()
                        .map(gameCharacter ->
                                new GameCharListCell(game.id, gameCharacter).invoke())
                        .collect(Collectors.toList());

                listChar.getChildren().setAll(FXCollections.observableArrayList(list));

            }
        });

    }


    public void load(Game game) {


        rootContainer.setVvalue(0);

        this.game = game;

        headerController.load(game);

        txtIntro.setText(game.intro);
        txtStory.setText(game.story);

//        var list = game.gameCharacters.stream()
//                .map(gameCharacter ->
//                        new GameCharListCell(game.id, gameCharacter).invoke())
//                .collect(Collectors.toList());

//        listChar.getChildren().setAll(FXCollections.observableArrayList(list));

        charListByGameService.restart();

        simpleImgController.load(game);

        logger.debug("{}", game);


    }


    private class GameCharListCell {
        private Game.GameCharacter gameCharacter;
        private int gameid;

        public GameCharListCell(int gameid, Game.GameCharacter gameCharacter) {
            this.gameid = gameid;
            this.gameCharacter = gameCharacter;
        }

        public Region invoke() {
            var loader = new FXMLLoaderProxy<Region, GameCharListCellController>("view/game_explorer/detail/char_list_cell.fxml");
            var controller = loader.controller;
            controller.gameId = gameid;
            controller.gameChar = gameCharacter;
            controller.init();
            return loader.node;
        }
    }
}
