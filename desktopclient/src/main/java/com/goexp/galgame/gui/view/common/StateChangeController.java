package com.goexp.galgame.gui.view.common;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.view.task.ChangeGameTask;
import com.goexp.galgame.gui.model.Game;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StateChangeController {

    private final Logger logger = LoggerFactory.getLogger(StateChangeController.class);

    @FXML
    private ChoiceBox<GameState> choiceState;

    private ChangeListener<GameState> listener;
    private Game targetGame;
    private Service<Void> changeGameStateService = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new ChangeGameTask.Like(targetGame);
        }
    };

    @FXML
    private void initialize() {
        var types = Stream.of(
                GameState.values()
        )
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        choiceState.setConverter(new StringConverter<>() {
            public String toString(GameState object) {
                return object.getName();
            }

            public GameState fromString(String string) {
                return GameState.from(string);
            }
        });

        choiceState.setItems(FXCollections.observableArrayList(types));


        listener = (o, oldValue, newValue) -> {

            if (newValue != null) {
                logger.debug("<Action>Value:{},New:{}", choiceState.getValue(), newValue);

                targetGame.state.set(newValue);

                changeGameStateService.restart();
            }
        };
    }

    public void load(Game game) {
        choiceState.valueProperty().removeListener(listener);
        choiceState.setValue(game.state.get());
        targetGame = game;
        choiceState.valueProperty().addListener(listener);
    }
}
