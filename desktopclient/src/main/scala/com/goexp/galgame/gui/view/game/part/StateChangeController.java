package com.goexp.galgame.gui.view.game.part;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.game.ChangeGameTask;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.task.TaskService;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StateChangeController extends DefaultController {

    @FXML
    private ChoiceBox<GameState> choiceState;

    private ChangeListener<GameState> listener;
    private Game targetGame;
    private Service<Void> changeGameStateService = new TaskService<>(() -> new ChangeGameTask.Like(targetGame));


    protected void initialize() {
        var types = Stream.of(
                GameState.values()
        )
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        choiceState.setConverter(new StringConverter<>() {
            public String toString(GameState gameState) {
                return gameState.name;
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
