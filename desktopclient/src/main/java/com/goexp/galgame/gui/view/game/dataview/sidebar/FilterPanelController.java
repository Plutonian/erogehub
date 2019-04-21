package com.goexp.galgame.gui.view.game.dataview.sidebar;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FilterPanelController extends FilterController<Game> {

    @FXML
    private FlowPane starbox;

    @FXML
    private FlowPane statebox;

    protected void initialize() {

        reset();
    }

    public void reset() {
        var nodes = Stream.of(GameState.values())
                .sorted(Comparator.comparing(GameState::getValue).reversed())
                .map(gameType -> {
                    var btn = new CheckBox(gameType.getName());
                    btn.setUserData(gameType);

                    if (gameType.getValue() > GameState.BLOCK.getValue())
                        btn.setSelected(true);

                    return btn;
                }).collect(Collectors.toList());

        statebox.getChildren().setAll(nodes);


        var starnodes = IntStream.rangeClosed(0, 5)
                .boxed()
                .sorted(Comparator.comparing(Integer::intValue).reversed())
                .map(star -> {
                    var btn = new CheckBox(star.toString());

                    btn.setSelected(true);

                    btn.setUserData(star);

                    return btn;
                }).collect(Collectors.toList());

        starbox.getChildren().setAll(starnodes);

        setP();
    }

    @FXML
    private void SetFilter_OnAction(ActionEvent event) {
        setP();

        onSetProperty.set(true);
        onSetProperty.set(false);
    }

    private void setP() {
        var stars = starbox.getChildren().stream()
                .filter(node -> ((CheckBox) node).isSelected())
                .map(node -> Integer.parseInt(((CheckBox) node).getText()))
                .collect(Collectors.toSet());

        var states = statebox.getChildren().stream()
                .filter(node -> ((CheckBox) node).isSelected())
                .map(node -> (GameState) (node.getUserData()))
                .collect(Collectors.toSet());

        if (stars.isEmpty())
            stars.add(0);

        predicate = game -> stars.contains(game.star) && states.contains(game.state.get());
    }

    @Override
    public void init(List<Game> filteredGames) {

    }
}
