package com.goexp.galgame.gui.view.game.detailview.part;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.game.ChangeGameTask;
import com.goexp.galgame.gui.util.DefaultController;
import com.goexp.galgame.gui.util.TaskService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class StarChoiceBarController extends DefaultController {

    public BooleanProperty onStarChangeProperty = new SimpleBooleanProperty(false);
    private Game targetGame;
    private ChangeListener<Toggle> handler;
    private List<ToggleButton> list;
    @FXML
    private HBox groupLikeCon;
    private ToggleGroup groupLike = new ToggleGroup();
    private Service<Void> changeStarService = new TaskService<>(() -> new ChangeGameTask.Star(targetGame));


    protected void initialize() {

        list = IntStream.rangeClosed(1, 5).boxed()
                .map(star -> {
                    var toggle = new ToggleButton();
                    toggle.setUserData(star);
                    toggle.setText(star.toString());
//                    toggle.setGraphic(new ImageView(image));
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
                targetGame.star = 0;
                changeStarService.restart();
            } else {
                logger.debug("New:{}", newV.getUserData());

                targetGame.star = (int) newV.getUserData();
                changeStarService.restart();
            }

            onStarChangeProperty.set(true);
            onStarChangeProperty.set(false);
        });

    }


    public void load(Game game) {
        this.targetGame = game;

        loadDetailPage(game);
    }

    private void loadDetailPage(Game g) {

        targetGame = g;

        groupLike.selectedToggleProperty().removeListener(handler);

        list.forEach(bt -> {
            bt.setSelected(false);
        });

        list.stream().filter(btn -> {
            return (int) (btn.getUserData()) == g.star;
        }).findAny().ifPresent((targetBtn) -> {
            targetBtn.setSelected(true);
        });


        groupLike.selectedToggleProperty().addListener(handler);

    }


}
