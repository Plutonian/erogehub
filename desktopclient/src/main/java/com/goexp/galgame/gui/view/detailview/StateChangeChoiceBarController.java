package com.goexp.galgame.gui.view.detailview;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.view.task.ChangeGameTask;
import com.goexp.galgame.gui.model.Game;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StateChangeChoiceBarController {
    private final Logger logger = LoggerFactory.getLogger(StateChangeChoiceBarController.class);

    private Game targetGame;

    private ChangeListener<Toggle> handler;

    private List<ToggleButton> list;


    @FXML
    private HBox groupLikeCon;

    private ToggleGroup groupLike = new ToggleGroup();

    private Service<Void> changeGameStateService = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new ChangeGameTask.Like(targetGame);
        }
    };

    @FXML
    private void initialize() {


        list = Stream.of(GameState.values())
                .filter(gameType -> gameType != GameState.UNCHECKED)
                .map(gameType -> {
                    var toggle = new ToggleButton();
                    toggle.setUserData(gameType);
                    toggle.setText(gameType.getName());
                    toggle.setToggleGroup(groupLike);

                    return toggle;
                })
                .collect(Collectors.toList());

        groupLikeCon.getChildren().setAll(list);

//        btnHope.setUserData(GameState.HOPE);
//        btnLike.setUserData(GameState.LIKE);
//        btnPass.setUserData(GameState.PASS);
//        btnDefault.setUserData(GameState.UNCHECKED);
//        btnNormal.setUserData(GameState.NORMAL);

//        list = List.of(btnLike, btnPass, btnDefault, btnNormal, btnHope);


        handler = ((o, oldV, newV) -> {
            if (newV == null) {
                targetGame.state.set(GameState.UNCHECKED);
                changeGameStateService.restart();
            } else {
                logger.debug("New:{}", newV.getUserData());

                targetGame.state.set((GameState) newV.getUserData());
                changeGameStateService.restart();
            }
        });

    }


    public void load(Game game) {
        this.targetGame = game;

        groupLike.selectedToggleProperty().removeListener(handler);

        list.forEach(bt -> {
            bt.setSelected(false);
        });

        list.stream().filter(btn -> {
            return (btn.getUserData()) == game.state.get();
        }).findAny().ifPresent((targetBtn) -> {
            targetBtn.setSelected(true);
        });


        groupLike.selectedToggleProperty().addListener(handler);

    }


}
