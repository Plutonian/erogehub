package com.goexp.galgame.gui.view.game.detailview.part;

import com.goexp.galgame.common.model.game.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.game.change.Like;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StateChangeChoiceBarController extends DefaultController {

    private Game targetGame;

    private ChangeListener<Toggle> handler;

    private List<ToggleButton> list;


    @FXML
    private HBox groupLikeCon;

    private final ToggleGroup groupLike = new ToggleGroup();

    private final Service<Void> changeGameStateService = new TaskService<>(() -> new Like(targetGame));


    protected void initialize() {


        list = Stream.of(GameState.values())
                .filter(gameType -> gameType != GameState.UNCHECKED)
                .map(gameType -> {
                    var toggle = new ToggleButton();
                    toggle.setUserData(gameType);
                    toggle.setText(gameType.name);
                    toggle.setToggleGroup(groupLike);

                    return toggle;
                })
                .collect(Collectors.toList());

        groupLikeCon.getChildren().setAll(list);

//        btnHope.setUserData(GameState.HOPE);
//        btnLike.setUserData(GameState.LIKE);
//        btnPass.setUserData(GameState.BLOCK);
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

        list.forEach(bt -> bt.setSelected(false));

        list.stream().filter(btn -> (btn.getUserData()) == game.state.get()).findAny().ifPresent((targetBtn) -> targetBtn.setSelected(true));


        groupLike.selectedToggleProperty().addListener(handler);

    }


}
