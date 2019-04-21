package com.goexp.galgame.gui.view.game.dataview.imglistview;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.DefaultController;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

import java.util.stream.Collectors;

public class GameImgListController extends DefaultController {

    final private int pageSize = 5;
    @FXML
    private FlowPane imgList;
    @FXML
    private ScrollPane rootPanel;
    private ObservableList<Game> cacheList;
    private int index = 1;
    private int page = 0;


    protected void initialize() {

        rootPanel.vvalueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null && newValue.doubleValue() > 0.8 * rootPanel.getVmax()) {
                loadItem();
            }
        });
    }

    public void load(ObservableList<Game> item) {

        cacheList = item;
        index = 1;
        page = (int) Math.ceil(cacheList.size() / (double) pageSize);
        rootPanel.setVvalue(rootPanel.getVmin());

        imgList.getChildren().clear();

        loadItem();

    }


    private void loadItem() {

        if (index <= page) {

            var nodes = cacheList.stream()
                    .skip((index - 1) * pageSize)
                    .limit(pageSize)
                    .map(game -> {
                        final var loader = new FXMLLoaderProxy<Region, GameImgListCellController>("view/game_explorer/listview/img/img_list_cell.fxml");

                        loader.controller.load(game);
                        return loader.node;
                    })
                    .collect(Collectors.toUnmodifiableList());

            index++;

            imgList.getChildren().addAll(nodes);
        }

    }


}
