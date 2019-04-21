package com.goexp.galgame.gui.view.game.detailview;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.game.GameImgListTask;
import com.goexp.galgame.gui.util.DefaultController;
import com.goexp.galgame.gui.util.Images;
import com.goexp.galgame.gui.util.TaskService;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;


public class SimpleImgController extends DefaultController {

    private Game game;

    @FXML
    private ListView<Game.GameImg> listSmallSimple;

    @FXML
    private ImageView largeSimple;


    private Service<ObservableList<Game.GameImg>> imgListService = new TaskService<>(() -> new GameImgListTask(game.id));


    protected void initialize() {

        ChangeListener<Throwable> exceptionListener = ((observable, oldValue, newValue) -> {
            if (newValue != null)
                newValue.printStackTrace();
        });


        imgListService.exceptionProperty().addListener(exceptionListener);
        imgListService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                listSmallSimple.setItems(FXCollections.observableArrayList(newValue));

                if (newValue.size() > 0) {
                    listSmallSimple.getSelectionModel().select(0);
                }
            }
        });

        listSmallSimple.setCellFactory(gameImgListView -> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Game.GameImg item, boolean empty) {
                    super.updateItem(item, empty);

                    setGraphic(null);
                    setText(null);

                    if (!empty) {
                        setGraphic(new ImageView(Images.GameImage.Simple.small(game.id, item.index, item.src)));
                    }

                }
            };
        });

        listSmallSimple.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, simpleLargeImage) -> {

            if (simpleLargeImage != null) {
                largeSimple.setImage(Images.GameImage.Simple.large(game.id, simpleLargeImage.index, simpleLargeImage.src));
            }
        });

    }


    public void load(Game game) {

        this.game = game;
        listSmallSimple.getSelectionModel().clearSelection();
        largeSimple.setImage(null);


//        listSmallSimple.setItems(FXCollections.observableArrayList(game.gameImgs));
//
//        if (game.gameImgs.size() > 0) {
//            listSmallSimple.getSelectionModel().select(0);
//        }

        imgListService.restart();

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
